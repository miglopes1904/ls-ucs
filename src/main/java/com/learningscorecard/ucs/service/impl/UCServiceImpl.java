package com.learningscorecard.ucs.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.learningscorecard.ucs.client.OntologyClient;
import com.learningscorecard.ucs.client.StudentClient;
import com.learningscorecard.ucs.exception.LSException;
import com.learningscorecard.ucs.model.dto.*;
import com.learningscorecard.ucs.model.dto.teacher.UCDTO4Teacher;
import com.learningscorecard.ucs.model.entity.*;
import com.learningscorecard.ucs.model.mapper.UCMapper;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
import com.learningscorecard.ucs.model.request.RemoveUsersRequest;
import com.learningscorecard.ucs.model.request.ontology.CreateOntologyUCRequest;
import com.learningscorecard.ucs.model.request.ontology.Mapping;
import com.learningscorecard.ucs.model.request.ontology.SyllabusContent;
import com.learningscorecard.ucs.repository.UCRepository;
import com.learningscorecard.ucs.service.UCService;
import com.learningscorecard.ucs.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
public class UCServiceImpl implements UCService {

    private static final String PLANNING_TEXT = "From START_MONTH START_DAY to END_MONTH END_DAY";

    private final UCRepository repository;
    private final UCMapper mapper;
    private final EntityUtils entityUtils;
    private final OntologyClient ontologyClient;
    private final StudentClient studentClient;

    public UCServiceImpl(UCRepository repository, UCMapper mapper, EntityUtils entityUtils, OntologyClient ontologyClient, StudentClient studentClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityUtils = entityUtils;
        this.ontologyClient = ontologyClient;
        this.studentClient = studentClient;
    }

    @Override
    public List<UCDTO4Teacher> getAll() {
        return mapper.toDTOs(repository.findAll());
    }

    @Override
    public UCDTO getByID(UUID id, Authentication authentication) {
        String type = authentication.getAuthorities().stream().findFirst().get().getAuthority();

        UC response = getOrElseThrow(id);
        Counts counts = new Counts();

        List<Mapping> mappings = ontologyClient.getMappings(id);
        List<SyllabusContent> contents = ontologyClient.getContents(id);
        counts.setContents((int) contents.stream()
                .filter(syllabusContent -> syllabusContent.getLevel() == 1).count());

        List<CalendarEntry> calendarEntries = new ArrayList<>();
        List<PlanningEntry> planningEntries = new ArrayList<>();

        UCDTO dto;

        if (type.equals("ROLE_TEACHER") || type.equals("ROLE_ADMIN"))
            dto = mapper.toDTO(response);
        else
            dto = mapper.toDTO4Student(response);

        dto.getQuests().forEach(quest -> {
            calendarEntries.add(mapCalendarEntry(quest));
            planningEntries.add(mapPlanningEntry(quest));

            switch (quest.getType()) {
                case "Class Attendance":
                    counts.addClass();
                    break;
                case "Practical Assignment":
                    counts.addPractical();
                    break;
                case "Quiz":
                    counts.addQuiz();
                    break;
                case "Exercise":
                    counts.addExercise();
                    break;
            }

        });

        calendarEntries.sort(Comparator.comparing(CalendarEntry::getDate));
        planningEntries.sort(Comparator.comparing(PlanningEntry::getDate));

        dto.setCounts(counts);
        dto.setMappings(mappings);
        dto.setContents(contents);
        dto.setCalendar(calendarEntries);
        dto.setPlanning(planningEntries);
        dto.setDifficulties(getDifficulties(id, response.getStudents()));

        return dto;
    }

    @Override
    public String create(CreateUCRequest request, Authentication authentication) {

        if (repository.existsByNameAndAcademicYear(request.getName(), request.getAcademicYear()))
            throw new LSException("UC already exists", HttpStatus.BAD_REQUEST);

        UC uc = UC.builder().name(request.getName()).acronym(request.getAcronym())
                .alliances(request.getAlliances()).semester(request.getSemester())
                .academicYear(request.getAcademicYear()).build();

        if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_TEACHER")) {
            Teacher teacher = entityUtils.getReference(Teacher.class,
                    UUID.fromString((String) authentication.getPrincipal()));
            uc.getTeachers().add(teacher);
        }

        uc = repository.save(uc);

        CreateOntologyUCRequest createUCRequest = new CreateOntologyUCRequest(uc.getId(), uc.getName());
        ontologyClient.createUC(createUCRequest);

        return "UC was created successfully";
    }

    @Override
    public String delete(UUID id) {
        UC uc = getOrElseThrow(id);
        try {
        ontologyClient.deleteUC(id);
        studentClient.removeStudents(RemoveUsersRequest.builder()
                .students(uc.getStudents().stream().map(Student::getId).toList())
                .uc(id).build());
        } catch (RuntimeException exception) {
            throw new LSException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        repository.deleteById(id);
        return "UC was deleted successfully";
    }

    @Override
    public List<JournalEntry> getJournal(UUID id) {
        UC uc = getOrElseThrow(id);
        List<JournalEntry> response = new ArrayList<>();
        uc.getStudents().forEach(student -> {

                Optional<Progress> progress = getProgress(id, student);

                if (progress.isPresent()) {
                    progress.get().getCompletedQuests().forEach(completedQuest -> {
                        String type = completedQuest.getType();
                        if (!type.equals("Class Attendance") && !type.equals("Event")) {
                            Optional<JournalEntry> journalOptional = response.stream()
                                    .filter(journalEntry -> journalEntry.getId().compareTo(completedQuest.getId()) == 0)
                                    .findFirst();
                            JournalStudent journalStudent = JournalStudent.builder()
                                    .username(student.getUsername())
                                    .validated(completedQuest.getValidated())
                                    .XPs(completedQuest.getValue())
                                    .grade(completedQuest.getGrade())
                                    .difficulty(completedQuest.getDifficulty()).build();

                            if (journalOptional.isEmpty()) {
                                response.add(JournalEntry.builder()
                                        .id(completedQuest.getId())
                                        .date(completedQuest.getStartDate())
                                        .title(completedQuest.getTitle())
                                        .students(Lists.newArrayList(journalStudent)).build());
                            } else {
                                JournalEntry journalEntry = journalOptional.get();
                                journalEntry.getStudents().add(journalStudent);
                            }
                        }
                    });
                    response.sort(Comparator.comparing(JournalEntry::getDate));
                }
        });
        return response;
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard(UUID id, String type) {

        List<LeaderboardEntry> response = new ArrayList<>();
        UC uc = getOrElseThrow(id);

        if(type.equals("guilds")) {
            uc.getGuilds().forEach(guild -> {
                Long total = guild
                        .getStudents().stream()
                        .mapToLong(student -> {
                            Optional<Progress> progress = getProgress(id, student);
                            if (progress.isPresent())
                                return progress.get().getValue();
                            else
                                return 0L;
                        } ).sum();

                response.add(new LeaderboardEntry(
                        0,
                        guild.getName(),
                        guild.getAlliance(),
                        total,
                        null,
                        null));
            });
        } else if(type.equals("all")) {
            uc.getStudents().forEach(student -> {
                LeaderboardEntry.LeaderboardEntryBuilder builder = LeaderboardEntry.builder();
                Optional<Progress> progress = getProgress(id, student);

                Avatar avatar = student.getAvatars().stream().filter(avatar1 -> avatar1.getUc()
                                .compareTo(uc.getId()) == 0)
                        .findFirst()
                        .orElse(new Avatar("USER"));

                Alliance alliance = getAlliance(uc, student);

                builder.name(student.getUsername())
                        .avatar(avatar.getValue()).alliance(alliance.getName());

                if (progress.isPresent()) {
                    builder.title(progress.get().getRank()).XP(progress.get().getValue());
                }

                response.add(builder.build());

            });

        } else {
            getLeaderboardByType(id, response, uc, type);
        }

        return Streams.mapWithIndex(
                response.stream().sorted(Comparator.comparing(LeaderboardEntry::getXP).reversed()),
                (entry, index) -> {
                    entry.setIndex((int) index + 1);
                    return entry;
                }).toList();

        //response.sort(Comparator.comparing(LeaderboardEntry::getXP).reversed());

        //return response;
    }

    private List<DifficultyEntry> getDifficulties(UUID id, List<Student> students) {

        List<DifficultyEntry> response = new ArrayList<>();

        students.stream().forEach(student -> addDifficultyEntry(response,
                student.getDifficulties().stream()
                        .filter(difficulty -> difficulty.getUc().compareTo(id) == 0).findFirst()));

        response.forEach(difficultyEntry -> {
            difficultyEntry.setMean((int) Math.round(difficultyEntry.getDifficulties().stream()
                    .mapToInt(Integer::intValue)
                    .average().getAsDouble()));
            difficultyEntry.setTotal((int) difficultyEntry.getDifficulties().size());
        });
        return response;
    }

    private void addDifficultyEntry(List<DifficultyEntry> difficultyEntries, Optional<Difficulty> difficulty) {

        if (difficulty.isPresent()) {
            difficulty.get().getContents().stream().forEach(contentDifficulty -> {
                if(contentDifficulty.getValue() >= 0) {
                    Optional<DifficultyEntry> difficultyEntryOptional =
                            difficultyEntries.stream()
                                    .filter(difficultyEntry ->
                                            difficultyEntry.getContent()
                                                    .compareTo(contentDifficulty.getId()) == 0)
                                    .findFirst();

                    if (difficultyEntryOptional.isPresent()) {
                        difficultyEntryOptional.get().getDifficulties().add(contentDifficulty.getValue());
                    } else {
                        difficultyEntries.add(DifficultyEntry.builder()
                                .content(contentDifficulty.getId())
                                .difficulties(Lists.newArrayList(contentDifficulty.getValue())).build());
                    }
                }
            });

        }

    }

    private void getLeaderboardByType(UUID id, List<LeaderboardEntry> response, UC uc, String type) {
        uc.getStudents().forEach(student -> {

            LeaderboardEntry.LeaderboardEntryBuilder builder = LeaderboardEntry.builder();
            Avatar avatar = student.getAvatars().stream().filter(avatar1 -> avatar1.getUc()
                            .compareTo(uc.getId()) == 0)
                    .findFirst().orElse(new Avatar("USER"));
            Alliance alliance = getAlliance(uc, student);

            builder.name(student.getUsername())
                    .avatar(avatar.getValue()).alliance(alliance.getName());


            Optional<Progress> progress = getProgress(id, student);

            if (progress.isPresent()) {
                Long total = progress.get()
                        .getCompletedQuests().stream()
                        .filter(completedQuest -> completedQuest.getValidated()
                                && completedQuest.getType().equals(StringUtils.capitalize(type)))
                        .mapToLong(CompletedQuest::getValue).sum();


                builder.title(progress.get().getRank()).XP(total);
            }

            response.add(builder.build());
        });
    }

    private Alliance getAlliance(UC uc, Student student) {
        return student.getAlliances().stream().filter(alliance1 -> alliance1.getUc()
                        .compareTo(uc.getId()) == 0).findFirst()
                .orElseThrow(() ->
                        new LSException("User " + student.getUsername()
                                + " does not belong to an alliance"));
    }

    private Optional<Progress> getProgress(UUID id, Student student) {
        return student.getProgresses().stream()
                .filter(progress1 -> progress1.getUc().compareTo(id) == 0).findFirst();
    }

    private UC getOrElseThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new LSException("UC does not exist", HttpStatus.BAD_REQUEST));
    }

    private CalendarEntry mapCalendarEntry(QuestDTO quest) {
        return new CalendarEntry(quest.getTitle(), quest.getStartDate());
    }

    private PlanningEntry mapPlanningEntry(QuestDTO quest) {
        String text = PLANNING_TEXT
                .replace("START_MONTH", StringUtils.capitalize(quest.getStartDate().getMonth().name().toLowerCase()))
                .replace("START_DAY", String.valueOf(quest.getStartDate().getDayOfMonth()))
                .replace("END_MONTH", StringUtils.capitalize(quest.getEndDate().getMonth().name().toLowerCase()))
                .replace("END_DAY", String.valueOf(quest.getEndDate().getDayOfMonth()));
        return PlanningEntry.builder()
                .title(text)
                .cardTitle(quest.getTitle())
                .cardSubtitle(quest.getSummary())
                .week(quest.getWeek())
                .date(quest.getStartDate()).build();
    }
}

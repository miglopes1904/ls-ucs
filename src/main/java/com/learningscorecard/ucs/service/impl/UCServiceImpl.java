package com.learningscorecard.ucs.service.impl;

import com.google.common.collect.Lists;
import com.learningscorecard.ucs.client.OntologyClient;
import com.learningscorecard.ucs.exception.LSException;
import com.learningscorecard.ucs.model.dto.*;
import com.learningscorecard.ucs.model.entity.*;
import com.learningscorecard.ucs.model.mapper.UCMapper;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
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

@Service
@Transactional
public class UCServiceImpl implements UCService {

    private static final String PLANNING_TEXT = "From START_MONTH START_DAY to END_MONTH END_DAY";

    private final UCRepository repository;
    private final UCMapper mapper;
    private final EntityUtils entityUtils;
    private final OntologyClient ontologyClient;

    public UCServiceImpl(UCRepository repository, UCMapper mapper, EntityUtils entityUtils, OntologyClient ontologyClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityUtils = entityUtils;
        this.ontologyClient = ontologyClient;
    }

    @Override
    public List<UCDTO> getAll() {
        return mapper.toDTOs(repository.findAll());
    }

    @Override
    public UCDTO getByID(UUID id) {
        UC response = getOrElseThrow(id);
        Counts counts = new Counts();

        List<Mapping> mappings = ontologyClient.getMappings(id);
        List<SyllabusContent> contents = ontologyClient.getContents(id);
        counts.setContents((int) contents.stream().count());

        List<CalendarEntry> calendarEntries = new ArrayList<>();
        List<PlanningEntry> planningEntries = new ArrayList<>();

        UCDTO dto =  mapper.toDTO(response);

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
        repository.deleteById(id);
        ontologyClient.deleteUC(id);
        return "UC was deleted successfully";
    }

    @Override
    public List<JournalEntry> getJournal(UUID id) {
        UC uc = getOrElseThrow(id);
        List<JournalEntry> response = new ArrayList<>();
        uc.getStudents().forEach(student -> {

                Progress progress = getProgress(id, student);

                progress.getCompletedQuests().forEach(completedQuest -> {
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

        });
        response.sort(Comparator.comparing(JournalEntry::getDate));

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
                        .mapToLong(student -> getProgress(id, student)
                                .getValue()).sum();

                response.add(new LeaderboardEntry(guild.getId(),
                        guild.getName(),
                        guild.getAlliance(),
                        total,
                        null,
                        null));
            });
        } else if(type.equals("all")) {
            uc.getStudents().forEach(student -> {

                LeaderboardEntry.LeaderboardEntryBuilder builder = LeaderboardEntry.builder();
                Avatar avatar = student.getAvatars().stream().filter(avatar1 -> avatar1.getUc()
                                .compareTo(uc.getId()) == 0)
                                .findFirst()
                                .orElse(new Avatar("USER"));

                Alliance alliance = getAlliance(uc, student);

                builder.id(student.getId()).name(student.getUsername())
                        .avatar(avatar.getValue()).alliance(alliance.getName());

                Progress progress = getProgress(id, student);

                builder.title(progress.getRank()).XP(progress.getValue());


                response.add(builder.build());
            });

        } else {
            getLeaderboardByType(id, response, uc, type);
        }

        response.sort(Comparator.comparing(LeaderboardEntry::getXP).reversed());
        return response;
    }

    private void getLeaderboardByType(UUID id, List<LeaderboardEntry> response, UC uc, String type) {
        uc.getStudents().forEach(student -> {

            LeaderboardEntry.LeaderboardEntryBuilder builder = LeaderboardEntry.builder();
            Avatar avatar = student.getAvatars().stream().filter(avatar1 -> avatar1.getUc()
                            .compareTo(uc.getId()) == 0)
                    .findFirst().orElse(new Avatar("USER"));
            Alliance alliance = getAlliance(uc, student);

            builder.id(student.getId()).name(student.getUsername())
                    .avatar(avatar.getValue()).alliance(alliance.getName());

            Progress progress = getProgress(id, student);

            Long total = progress
                    .getCompletedQuests().stream()
                    .filter(completedQuest -> completedQuest.getValidated()
                            && completedQuest.getType().equals(StringUtils.capitalize(type)))
                    .mapToLong(CompletedQuest::getValue).sum();


            builder.title(progress.getRank()).XP(total);

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

    private Progress getProgress(UUID id, Student student) {
        return student.getProgresses().stream()
                .filter(progress1 -> progress1.getUc().compareTo(id) == 0).findFirst()
                .orElseThrow(() ->
                        new LSException("User " + student.getUsername()
                                + " does not have progress for this UC"));
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
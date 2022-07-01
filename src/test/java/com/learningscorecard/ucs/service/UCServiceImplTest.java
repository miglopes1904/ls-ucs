package com.learningscorecard.ucs.service;

import com.google.common.collect.Lists;
import com.learningscorecard.ucs.client.OntologyClient;
import com.learningscorecard.ucs.exception.LSException;
import com.learningscorecard.ucs.model.dto.Counts;
import com.learningscorecard.ucs.model.dto.JournalEntry;
import com.learningscorecard.ucs.model.dto.LeaderboardEntry;
import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.dto.student.UCDTO4Student;
import com.learningscorecard.ucs.model.dto.teacher.UCDTO4Teacher;
import com.learningscorecard.ucs.model.entity.*;
import com.learningscorecard.ucs.model.mapper.*;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
import com.learningscorecard.ucs.model.request.ontology.Mapping;
import com.learningscorecard.ucs.model.request.ontology.MappingPOJO;
import com.learningscorecard.ucs.model.request.ontology.SyllabusContent;
import com.learningscorecard.ucs.repository.UCRepository;
import com.learningscorecard.ucs.service.impl.UCServiceImpl;
import com.learningscorecard.ucs.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UCServiceImplTest {

    private static final String ROLE_ = "ROLE_";
    private final UUID ID_1 = UUID.fromString("1bdd9c82-e902-4a45-9292-afd0fa7a2a16");
    private final UUID ID_AUTH = UUID.fromString("1bdd9c82-e902-4a45-9292-afd0fa7a2a16");
    private final UUID ID_NOT_FOUND = UUID.fromString("1bdd9c82-e902-4a45-9292-afd0fa7a2a17");

    private final UC UC_1 = UC.builder().name("Unit Course").academicYear("2022/2023").id(ID_1)
            .quests(List.of(
                    Quest.builder().id(UUID.randomUUID()).title("quest1").description("desc1")
                            .startDate(LocalDate.now())
                            .endDate(LocalDate.now()).type("Class Attendance")
                            .week(1).summary("summary").build(),
                    Quest.builder().id(UUID.randomUUID()).title("quest2").description("desc2")
                            .startDate(LocalDate.now())
                            .endDate(LocalDate.now()).type("Quiz")
                            .week(1).summary("summary").build(),
                    Quest.builder().id(UUID.randomUUID()).title("quest3").description("desc3")
                            .startDate(LocalDate.now())
                            .endDate(LocalDate.now()).type("Exercise")
                            .week(1).summary("summary").build(),
                    Quest.builder().id(UUID.randomUUID()).title("quest4").description("desc4")
                            .startDate(LocalDate.now())
                            .endDate(LocalDate.now()).type("Practical Assignment")
                            .week(1).summary("summary").build()
            ))
            .students(Lists.newArrayList(
                    Student.builder()
                            .id(UUID.randomUUID())
                            .alliances(List.of(
                                    new Alliance("MEI", ID_1)
                            ))
                            .email("EMAIL")
                            .name("STUDENT NAME")
                            .number(93123L)
                            .avatars(List.of(new Avatar("EAGLE", ID_1)))
                            .progresses(List.of(
                                    Progress.builder()
                                            .id(UUID.randomUUID())
                                            .uc(ID_1)
                                            .rank("rank")
                                            .value(15000L)
                                            .completedQuests(List.of(
                                                    CompletedQuest.builder()
                                                            .validated(Boolean.TRUE)
                                                            .difficulty(1)
                                                            .id(ID_1)
                                                            .endDate(LocalDate.now())
                                                            .grade("A")
                                                            .startDate(LocalDate.now())
                                                            .title("title")
                                                            .value(1500L)
                                                            .type("Quiz").build()
                                            )).build()
                            )).build()
            ))
            .teachers(List.of(Teacher.builder().name("TEACHER NAME").id(UUID.randomUUID())
                    .email("teacheremail").lastLogin(LocalDate.now()).build()))
            .guilds(List.of(
                    Guild.builder()
                            .id(ID_1)
                            .uc(ID_1)
                            .name("guild1")
                            .alliance("MEI")
                            .students(List.of(
                                    Student.builder()
                                            .id(UUID.randomUUID())
                                            .alliances(List.of(
                                                    new Alliance("MEI", ID_1)
                                            ))
                                            .progresses(List.of(
                                                    Progress.builder()
                                                            .id(UUID.randomUUID())
                                                            .uc(ID_1)
                                                            .rank("rank")
                                                            .value(15000L)
                                                            .completedQuests(List.of(
                                                                    CompletedQuest.builder()
                                                                            .validated(Boolean.TRUE)
                                                                            .difficulty(1)
                                                                            .id(ID_1)
                                                                            .endDate(LocalDate.now())
                                                                            .grade("A")
                                                                            .startDate(LocalDate.now())
                                                                            .title("title")
                                                                            .value(1500L)
                                                                            .type("Quiz").build()
                                                            )).build()
                                            )).build()
                            )).build()
            )).build();
    private final UC UC_2 = UC.builder().name("UC").academicYear("2022/2023").build();
    public final Authentication AUTH_ADMIN =
            new UsernamePasswordAuthenticationToken(ID_AUTH.toString(), null,
                    Arrays.asList(new SimpleGrantedAuthority(ROLE_ + "ADMIN")));
    public final Authentication AUTH_TEACHER =
            new UsernamePasswordAuthenticationToken(ID_AUTH.toString(), null,
                    Arrays.asList(new SimpleGrantedAuthority(ROLE_ + "TEACHER")));
    public final Authentication AUTH_STUDENT =
            new UsernamePasswordAuthenticationToken(ID_AUTH.toString(), null,
                    Arrays.asList(new SimpleGrantedAuthority(ROLE_ + "STUDENT")));
    private final CreateUCRequest REQUEST_OK = CreateUCRequest.builder().name("Unit Course")
            .academicYear("2022/2023").alliances(Arrays.asList("A", "B", "C")).acronym("UC").semester(1).build();
    private final CreateUCRequest REQUEST_NOK = CreateUCRequest.builder().name("UC")
            .academicYear("2022/2023").alliances(Arrays.asList("A", "B", "C")).acronym("UC").semester(1).build();
    private final UCRepository repository = mock(UCRepository.class);
    private final UCMapperImpl mapper = new UCMapperImpl(
            new StudentMapperImpl(), new TeacherMapperImpl(), new QuestMapperImpl(),
            new GuildMapperImpl(new ExtStudentMapperImpl())
            );
    private final EntityUtils entityUtils = mock(EntityUtils.class);
    private final OntologyClient ontologyClient = mock(OntologyClient.class);
    private UCServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UCServiceImpl(repository, mapper, entityUtils, ontologyClient);
        setupRepo();
    }

    @Test
    public void getUCsOK() {
        List<UCDTO4Teacher> response = service.getAll();

        assertEquals(2, response.size());
        assertEquals(UCDTO4Teacher.class, response.get(0).getClass());
    }

    @Test
    public void getUCByIdTeacherOK() {
        doReturn(List.of(new Mapping(UUID.randomUUID(), List.of(new MappingPOJO(UUID.randomUUID(), "title")))))
                .when(ontologyClient).getMappings(eq(ID_1));
        doReturn(List.of(SyllabusContent.builder().id(UUID.randomUUID()).title("title").level(1).build()))
                .when(ontologyClient).getContents(eq(ID_1));

        UCDTO response = service.getByID(ID_1, AUTH_TEACHER);

        assertEquals(ID_1, response.getId());
        Counts counts = response.getCounts();
        assertEquals(1, counts.getClassAttendances());
        assertEquals(1, counts.getQuizzes());
        assertEquals(1, counts.getExercises());
        assertEquals(1, counts.getPracticalAssignments());
        assertEquals(1, counts.getContents());
        assertEquals(4, response.getPlanning().size());
        assertEquals(4, response.getCalendar().size());
        assertEquals(UCDTO4Teacher.class, response.getClass());
    }

    @Test
    public void getUCByIdStudentOK() {
        doReturn(List.of(new Mapping(UUID.randomUUID(), List.of(new MappingPOJO(UUID.randomUUID(), "title")))))
                .when(ontologyClient).getMappings(eq(ID_1));
        doReturn(List.of(SyllabusContent.builder().id(UUID.randomUUID()).title("title").level(1).build()))
                .when(ontologyClient).getContents(eq(ID_1));

        UCDTO response = service.getByID(ID_1, AUTH_STUDENT);

        assertEquals(ID_1, response.getId());
        Counts counts = response.getCounts();
        assertEquals(1, counts.getClassAttendances());
        assertEquals(1, counts.getQuizzes());
        assertEquals(1, counts.getExercises());
        assertEquals(1, counts.getPracticalAssignments());
        assertEquals(1, counts.getContents());
        assertEquals(4, response.getPlanning().size());
        assertEquals(4, response.getCalendar().size());
        assertEquals(UCDTO4Student.class, response.getClass());
    }

    @Test
    public void getUCByIdNotFound() {

        LSException thrown = assertThrows(LSException.class, () -> service.getByID(ID_NOT_FOUND, AUTH_TEACHER));

        assertEquals("UC does not exist", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    public void createUCOK() {
        doReturn(UC.builder().id(UUID.randomUUID()).name("name").build()).when(repository).save(any());
        String response = service.create(REQUEST_OK, AUTH_ADMIN);
        assertEquals("UC was created successfully", response);
        //verify(entityUtils).getReference(eq(Teacher.class), eq())
        verify(repository).save(any(UC.class));
    }

    @Test
    public void createUCOKTeacher() {
        doReturn(UC.builder().id(UUID.randomUUID()).name("name").build()).when(repository).save(any());

        String response = service.create(REQUEST_OK, AUTH_TEACHER);
        assertEquals("UC was created successfully", response);
        verify(entityUtils).getReference(Teacher.class, ID_AUTH);
        verify(repository).save(any(UC.class));
    }

    @Test
    public void createUCNOK() {
        LSException thrown = assertThrows(LSException.class, () -> service.create(REQUEST_NOK, AUTH_ADMIN));

        assertEquals("UC already exists", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    public void getJournal() {

        List<JournalEntry> response = service.getJournal(ID_1);

        assertEquals(1, response.size());
        assertEquals("title", response.get(0).getTitle());
    }

    @Test
    public void getJournal2Students() {
        UC_1.getStudents().add(Student.builder()
                .id(UUID.randomUUID())
                .progresses(List.of(
                        Progress.builder()
                                .id(UUID.randomUUID())
                                .uc(ID_1)
                                .rank("rank")
                                .value(15000L)
                                .completedQuests(List.of(
                                        CompletedQuest.builder()
                                                .validated(Boolean.TRUE)
                                                .difficulty(1)
                                                .id(ID_1)
                                                .endDate(LocalDate.now())
                                                .grade("A")
                                                .startDate(LocalDate.now())
                                                .title("title")
                                                .value(1500L)
                                                .type("Quiz").build()
                                )).build()
                )).build());

        List<JournalEntry> response = service.getJournal(ID_1);

        assertEquals(2, response.get(0).getStudents().size());
    }

    @Test
    public void getLeaderboardGuilds() {

        List<LeaderboardEntry> response = service.getLeaderboard(ID_1, "guilds");

        assertEquals(1, response.size());
        assertEquals(15000L, response.get(0).getXP());
        assertEquals("guild1", response.get(0).getName());
        assertEquals("MEI", response.get(0).getAlliance());
    }

    @Test
    public void getLeaderboardAll() {

        List<LeaderboardEntry> response = service.getLeaderboard(ID_1, "all");

        assertEquals(1, response.size());
        assertEquals("To be defined", response.get(0).getName());
        assertEquals(15000L, response.get(0).getXP());
        assertEquals("MEI", response.get(0).getAlliance());
    }

    @Test
    public void getLeaderboardOther() {

        List<LeaderboardEntry> response = service.getLeaderboard(ID_1, "quiz");

        assertEquals(1, response.size());
        assertEquals("To be defined", response.get(0).getName());
        assertEquals(1500L, response.get(0).getXP());
        assertEquals("MEI", response.get(0).getAlliance());
    }

    @Test
    public void getLeaderboardOrdered() {

        UC_1.getStudents().add(Student.builder()
                .id(UUID.randomUUID())
                .alliances(List.of(
                        new Alliance("MEI", ID_1)
                ))
                .avatars(List.of(new Avatar("EAGLE", ID_1)))
                .progresses(List.of(
                        Progress.builder()
                                .id(UUID.randomUUID())
                                .uc(ID_1)
                                .rank("rank")
                                .value(17000L)
                                .completedQuests(List.of(
                                        CompletedQuest.builder()
                                                .validated(Boolean.TRUE)
                                                .difficulty(1)
                                                .id(ID_1)
                                                .endDate(LocalDate.now())
                                                .grade("A")
                                                .startDate(LocalDate.now())
                                                .title("title")
                                                .value(1500L)
                                                .type("Quiz").build()
                                )).build()
                )).build());

        List<LeaderboardEntry> response = service.getLeaderboard(ID_1, "all");

        assertEquals(2, response.size());
        assertEquals("To be defined", response.get(0).getName());
        assertEquals(17000L, response.get(0).getXP());
        assertEquals("MEI", response.get(0).getAlliance());
    }

    @Test
    public void getLeaderboardNoAlliance() {

        UC_1.getStudents().add(Student.builder()
                .id(UUID.randomUUID())
                .alliances(List.of(
                        new Alliance("MEI", UUID.randomUUID())
                ))
                .avatars(List.of(new Avatar("EAGLE", ID_1)))
                .progresses(List.of(
                        Progress.builder()
                                .id(UUID.randomUUID())
                                .uc(ID_1)
                                .rank("rank")
                                .value(17000L)
                                .completedQuests(List.of(
                                        CompletedQuest.builder()
                                                .validated(Boolean.TRUE)
                                                .difficulty(1)
                                                .id(ID_1)
                                                .endDate(LocalDate.now())
                                                .grade("A")
                                                .startDate(LocalDate.now())
                                                .title("title")
                                                .value(1500L)
                                                .type("Quiz").build()
                                )).build()
                )).build());

        LSException thrown = assertThrows(LSException.class, () -> service.getLeaderboard(ID_1, "all"));

        assertEquals("User To be defined does not belong to an alliance", thrown.getMessage());
    }

    @Test
    public void deleteUC() {
        String response = service.delete(ID_1);

        assertEquals("UC was deleted successfully", response);
        verify(repository).deleteById(ID_1);
    }

    private void setupRepo() {
        doReturn(Arrays.asList(UC_1, UC_2)).when(repository).findAll();
        doReturn(Optional.of(UC_1)).when(repository).findById(ID_1);
        doReturn(Optional.empty()).when(repository).findById(ID_NOT_FOUND);
        doReturn(Boolean.FALSE).when(repository).existsByNameAndAcademicYear(UC_1.getName(), UC_1.getAcademicYear());
        doReturn(Boolean.TRUE).when(repository).existsByNameAndAcademicYear(UC_2.getName(), UC_2.getAcademicYear());
    }
}

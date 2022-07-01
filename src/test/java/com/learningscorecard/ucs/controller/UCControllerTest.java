package com.learningscorecard.ucs.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.learningscorecard.ucs.client.OntologyClient;
import com.learningscorecard.ucs.exception.ExceptionResponseBody;
import com.learningscorecard.ucs.model.dto.Counts;
import com.learningscorecard.ucs.model.dto.JournalEntry;
import com.learningscorecard.ucs.model.dto.LeaderboardEntry;
import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.dto.student.UCDTO4Student;
import com.learningscorecard.ucs.model.entity.*;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
import com.learningscorecard.ucs.model.request.ontology.Mapping;
import com.learningscorecard.ucs.model.request.ontology.MappingPOJO;
import com.learningscorecard.ucs.model.request.ontology.SyllabusContent;
import com.learningscorecard.ucs.model.response.LSResponse;
import com.learningscorecard.ucs.repository.UCRepository;
import com.learningscorecard.ucs.security.jwt.JwtUtils;
import com.learningscorecard.ucs.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UCControllerTest {

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
                            .username("username")
                            .alliances(List.of(
                                    new Alliance("MEI", ID_1)
                            ))
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
    private final CreateUCRequest REQUEST_OK = CreateUCRequest.builder().name("Unit Course")
            .academicYear("2022/2023").alliances(Arrays.asList("A", "B", "C")).acronym("UC").semester(1).build();
    private final CreateUCRequest REQUEST_NOK = CreateUCRequest.builder().name("UC")
            .academicYear("2022/2023").alliances(Arrays.asList("A", "B", "C")).acronym("UC").semester(1).build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    JwtUtils jwtUtils;

    @MockBean
    UCRepository repository;

    @MockBean
    OntologyClient ontologyClient;

    @Test
    public void getUCs() throws Exception{
        setUp();
        MvcResult result = mockMvc.perform(get("/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(2,
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<List<UCDTO>>>() {
                        }).getData().size());
    }

    @Test
    public void getUCById() throws Exception{
        doReturn(List.of(new Mapping(UUID.randomUUID(), "title", List.of(new MappingPOJO(UUID.randomUUID(), "title")))))
                .when(ontologyClient).getMappings(eq(ID_1));
        doReturn(List.of(SyllabusContent.builder().id(UUID.randomUUID()).title("title").level(1).build()))
                .when(ontologyClient).getContents(eq(ID_1));
        setUp();
        MvcResult result = mockMvc.perform(get("/"+ ID_1)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "TEACHER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

       UCDTO response =  mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<UCDTO>>() {
                        }).getData();

        assertEquals(ID_1, response.getId());
        Counts counts = response.getCounts();
        assertEquals(1, counts.getClassAttendances());
        assertEquals(1, counts.getQuizzes());
        assertEquals(1, counts.getExercises());
        assertEquals(1, counts.getPracticalAssignments());
        assertEquals(1, counts.getContents());
        assertEquals(4, response.getPlanning().size());
        assertEquals(4, response.getCalendar().size());

    }

    @Test
    public void getUCByIdStudent() throws Exception{
        doReturn(List.of(new Mapping(UUID.randomUUID(), "title", List.of(new MappingPOJO(UUID.randomUUID(), "title")))))
                .when(ontologyClient).getMappings(eq(ID_1));
        doReturn(List.of(SyllabusContent.builder().id(UUID.randomUUID()).title("title").level(1).build()))
                .when(ontologyClient).getContents(eq(ID_1));
        setUp();
        MvcResult result = mockMvc.perform(get("/"+ ID_1)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "STUDENT")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        UCDTO response =  mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LSResponse<UCDTO4Student>>() {
                }).getData();

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
    public void getUCByIdNotFound() throws Exception{
        setUp();
        MvcResult result = mockMvc.perform(get("/"+ ID_NOT_FOUND)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "TEACHER")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("UC does not exist",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    @Test
    public void createUCOK() throws Exception{
        setUp();
        doReturn(UC.builder().id(UUID.randomUUID()).name("name").build()).when(repository).save(any());
        MvcResult result = mockMvc.perform(post("/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN"))
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(REQUEST_OK)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("UC was created successfully",
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<String>>() {
                        }).getData());
    }

    @Test
    public void createUCNOK() throws Exception{
        setUp();
        MvcResult result = mockMvc.perform(post("/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN"))
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(REQUEST_NOK)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("UC already exists",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }


    @Test
    public void getJournal() throws Exception{
        setUp();

        MvcResult result = mockMvc.perform(get("/journal/" + UC_1.getId().toString())
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<JournalEntry> response = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LSResponse<List<JournalEntry>>>() {
                }).getData();

        assertEquals(1, response.size());
        assertEquals("title", response.get(0).getTitle());
    }

    @Test
    public void getJournal2Students() throws Exception{
        setUp();
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

        MvcResult result = mockMvc.perform(get("/journal/" + UC_1.getId().toString())
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<JournalEntry> response = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LSResponse<List<JournalEntry>>>() {
                }).getData();

        assertEquals(2, response.get(0).getStudents().size());
    }

    @Test
    public void getLeaderboardGuilds() throws Exception{
        setUp();

        MvcResult result = mockMvc.perform(get("/leaderboard/" + UC_1.getId().toString() + "/guilds")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<LeaderboardEntry> response = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LSResponse<List<LeaderboardEntry>>>() {
                }).getData();


        assertEquals(1, response.size());
        assertEquals(15000L, response.get(0).getXP());
        assertEquals("guild1", response.get(0).getName());
        assertEquals("MEI", response.get(0).getAlliance());
    }

    @Test
    public void getLeaderboardAll() throws Exception{
        setUp();
        MvcResult result = mockMvc.perform(get("/leaderboard/" + UC_1.getId().toString() + "/all")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<LeaderboardEntry> response = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LSResponse<List<LeaderboardEntry>>>() {
                }).getData();


        assertEquals(1, response.size());
        assertEquals("username", response.get(0).getName());
        assertEquals(15000L, response.get(0).getXP());
        assertEquals("MEI", response.get(0).getAlliance());
    }

    @Test
    public void getLeaderboardOther() throws Exception{
        setUp();

        MvcResult result = mockMvc.perform(get("/leaderboard/" + UC_1.getId().toString() + "/quiz")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<LeaderboardEntry> response = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LSResponse<List<LeaderboardEntry>>>() {
                }).getData();


        assertEquals(1, response.size());
        assertEquals("username", response.get(0).getName());
        assertEquals(1500L, response.get(0).getXP());
        assertEquals("MEI", response.get(0).getAlliance());
    }

    @Test
    public void getLeaderboardOrdered() throws Exception{
        setUp();
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

        MvcResult result = mockMvc.perform(get("/leaderboard/" + UC_1.getId().toString() + "/all")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<LeaderboardEntry> response = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LSResponse<List<LeaderboardEntry>>>() {
                }).getData();

        assertEquals(2, response.size());
        assertEquals("To be defined", response.get(0).getName());
        assertEquals(17000L, response.get(0).getXP());
        assertEquals("MEI", response.get(0).getAlliance());
    }

    @Test
    public void deleteUCOK() throws Exception{
        setUp();
        MvcResult result = mockMvc.perform(delete("/" + ID_1)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("UC was deleted successfully",
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<String>>() {
                        }).getData());
    }

    private void setUp() {
        doReturn(Arrays.asList(UC_1, UC_2)).when(repository).findAll();
        doReturn(Optional.of(UC_1)).when(repository).findById(ID_1);
        doReturn(Optional.empty()).when(repository).findById(ID_NOT_FOUND);
        doReturn(Boolean.FALSE).when(repository).existsByNameAndAcademicYear(UC_1.getName(), UC_1.getAcademicYear());
        doReturn(Boolean.TRUE).when(repository).existsByNameAndAcademicYear(UC_2.getName(), UC_2.getAcademicYear());
    }
}
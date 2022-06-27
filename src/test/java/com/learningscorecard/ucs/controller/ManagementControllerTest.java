package com.learningscorecard.ucs.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learningscorecard.ucs.exception.ExceptionResponseBody;
import com.learningscorecard.ucs.model.entity.GradeValue;
import com.learningscorecard.ucs.model.entity.Rank;
import com.learningscorecard.ucs.model.entity.UC;
import com.learningscorecard.ucs.model.entity.XP;
import com.learningscorecard.ucs.model.request.UpdateGradeRequest;
import com.learningscorecard.ucs.model.request.UpdateRankRequest;
import com.learningscorecard.ucs.model.request.UpdateXPRequest;
import com.learningscorecard.ucs.model.response.LSResponse;
import com.learningscorecard.ucs.repository.UCRepository;
import com.learningscorecard.ucs.security.jwt.JwtUtils;
import com.learningscorecard.ucs.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ManagementControllerTest {

    private UUID ID_NOT_EXIST = UUID.fromString("1bdd9c82-e902-4a45-9292-afd0fa7a2a15");
    private UUID ID_NOT_STARTED = UUID.fromString("1bdd9c82-e902-4a45-9292-afd0fa7a2a16");
    private UUID ID_STARTED = UUID.fromString("1bdd9c82-e902-4a45-9292-afd0fa7a2a17");
    private UUID ID_NOT_ACTIVE = UUID.fromString("1bdd9c82-e902-4a45-9292-afd0fa7a2a18");
    private final UC UC_NOT_STARTED = UC.builder().id(ID_NOT_STARTED).name("Unit Course").acronym("UC").build();
    private final UC UC_STARTED = UC.builder().id(ID_STARTED).name("Unit Course").acronym("UC")
            .started(Boolean.TRUE).build();
    private final UC UC_NOT_ACTIVE = UC.builder().id(ID_NOT_ACTIVE).name("Unit Course").acronym("UC")
            .active(Boolean.FALSE).build();
    private final UpdateRankRequest RANK_OK = UpdateRankRequest.builder().id(ID_STARTED)
            .ranks(Arrays.asList(new Rank("name", 78L))).build();
    private final UpdateRankRequest RANK_NOK = UpdateRankRequest.builder().id(ID_NOT_EXIST)
            .ranks(Arrays.asList(new Rank("name", 78L))).build();
    private final UpdateGradeRequest GRADE_OK = UpdateGradeRequest.builder().id(ID_STARTED)
            .grades(Arrays.asList(new GradeValue("name", 78))).build();
    private final UpdateGradeRequest GRADE_NOK = UpdateGradeRequest.builder().id(ID_NOT_EXIST)
            .grades(Arrays.asList(new GradeValue("name", 78))).build();
    private final UpdateXPRequest XP_OK = UpdateXPRequest.builder().id(ID_STARTED)
            .XPs(Arrays.asList(new XP("name", 7800L, 70))).build();
    private final UpdateXPRequest XP_NOK = UpdateXPRequest.builder().id(ID_NOT_EXIST)
            .XPs(Arrays.asList(new XP("name", 7800L, 70))).build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UCRepository repository;


    @Test
    public void start() throws Exception {
        setupRepo();
        MvcResult result = mockMvc.perform(put("/start/"+ ID_NOT_STARTED)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("UC was successfully started",
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<String>>() {
                        }).getData());
    }

    @Test
    public void startAlreadyStarted() throws Exception {
        setupRepo();
        MvcResult result = mockMvc.perform(put("/start/"+ ID_STARTED)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("UC is already started",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    @Test
    public void startNOK() throws Exception {
        setupRepo();
        MvcResult result = mockMvc.perform(put("/start/"+ ID_NOT_EXIST)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("UC does not exist",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    @Test
    public void close() throws Exception {
        setupRepo();
        MvcResult result = mockMvc.perform(put("/close/"+ ID_NOT_STARTED)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("UC was successfully closed",
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<String>>() {
                        }).getData());
    }

    @Test
    public void closedNotActive() throws Exception {
        setupRepo();
        MvcResult result = mockMvc.perform(put("/close/"+ ID_NOT_ACTIVE)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("UC is already closed",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    @Test
    public void closedNOK() throws Exception {
        setupRepo();
        MvcResult result = mockMvc.perform(put("/close/"+ ID_NOT_EXIST)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("UC does not exist",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    @Test
    public void updateRanks() throws Exception {
        setupRepo();
        MvcResult result = mockMvc.perform(put("/ranks/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER"))
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(RANK_OK)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("UC ranks successfully added",
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<String>>() {
                        }).getData());
    }

    @Test
    public void ranksNOK() throws Exception {
        setupRepo();
        MvcResult result =mockMvc.perform(put("/ranks/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER"))
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(RANK_NOK)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("UC does not exist",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    @Test
    public void updateGrades() throws Exception {
        setupRepo();
        MvcResult result = mockMvc.perform(put("/grades/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER"))
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(GRADE_OK)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("UC grades successfully added",
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<String>>() {
                        }).getData());
    }

    @Test
    public void gradesNOK() throws Exception {
        setupRepo();
        MvcResult result =mockMvc.perform(put("/grades/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER"))
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(GRADE_NOK)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("UC does not exist",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    @Test
    public void updateXPs() throws Exception {
        setupRepo();
        MvcResult result = mockMvc.perform(put("/xps/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER"))
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(XP_OK)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("UC XPs successfully added",
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<String>>() {
                        }).getData());
    }

    @Test
    public void XPsNOK() throws Exception {
        setupRepo();
        MvcResult result =mockMvc.perform(put("/xps/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_STARTED, "TEACHER"))
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(XP_NOK)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("UC does not exist",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    private void setupRepo() {
        doReturn(Optional.of(UC_NOT_STARTED)).when(repository).findById(ID_NOT_STARTED);
        doReturn(Optional.of(UC_STARTED)).when(repository).findById(ID_STARTED);
        doReturn(Optional.of(UC_NOT_ACTIVE)).when(repository).findById(ID_NOT_ACTIVE);
    }
}
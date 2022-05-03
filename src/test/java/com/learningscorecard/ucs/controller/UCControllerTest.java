package com.learningscorecard.ucs.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learningscorecard.ucs.exception.ExceptionResponseBody;
import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.entity.UC;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    private final UC UC_1 = UC.builder().name("Unit Course").academicYear("2022/2023").id(ID_1).build();
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


    @Test
    public void getUCs() throws Exception{
        setUp();
        MvcResult result = mockMvc.perform(get("/")
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "TEACHER")))
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
        setUp();
        MvcResult result = mockMvc.perform(get("/"+ ID_1)
                        .header("Authorization", "Bearer " +
                                jwtUtils.generate(ID_AUTH, "TEACHER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(ID_1,
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<LSResponse<UCDTO>>() {
                        }).getData().getId());
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
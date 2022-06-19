package com.learningscorecard.ucs.service;

import com.learningscorecard.ucs.exception.LSException;
import com.learningscorecard.ucs.model.entity.GradeValue;
import com.learningscorecard.ucs.model.entity.Rank;
import com.learningscorecard.ucs.model.entity.UC;
import com.learningscorecard.ucs.model.entity.XP;
import com.learningscorecard.ucs.model.request.UpdateGradeRequest;
import com.learningscorecard.ucs.model.request.UpdateRankRequest;
import com.learningscorecard.ucs.model.request.UpdateXPRequest;
import com.learningscorecard.ucs.repository.UCRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ManagementServiceTest {

    private final UCRepository repository = mock(UCRepository.class);
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
    private ManagementService service;

    @BeforeEach
    void setUp() {
        service = new ManagementService(repository);
        setupRepo();
    }

    @Test
    public void startUCOK() {
        String response = service.start(ID_NOT_STARTED);

        assertEquals("UC was successfully started", response);
    }

    @Test
    public void startUCAlreadyStarted() {
        LSException thrown = assertThrows(LSException.class, () -> service.start(ID_STARTED));
        assertEquals("UC is already started", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    public void startUCNOK() {
        LSException thrown = assertThrows(LSException.class, () -> service.start(ID_NOT_EXIST));
        assertEquals("UC does not exist", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    public void closeUCOK() {
        String response = service.close(ID_NOT_STARTED);

        assertEquals("UC was successfully closed", response);
    }

    @Test
    public void closeUCNotActive() {
        LSException thrown = assertThrows(LSException.class, () -> service.close(ID_NOT_ACTIVE));
        assertEquals("UC is already closed", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    public void closeUCNOK() {
        LSException thrown = assertThrows(LSException.class, () -> service.close(ID_NOT_EXIST));
        assertEquals("UC does not exist", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    public void updateUCRanksOK() {
        String response = service.updateRanks(RANK_OK);

        assertEquals("UC ranks successfully added", response);
    }


    @Test
    public void updateUCRanksNOK() {
        LSException thrown = assertThrows(LSException.class, () -> service.updateRanks(RANK_NOK));
        assertEquals("UC does not exist", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    public void updateUCGradesOK() {
        String response = service.updateGrades(GRADE_OK);

        assertEquals("UC grades successfully added", response);
    }


    @Test
    public void updateUCGradesNOK() {
        LSException thrown = assertThrows(LSException.class, () -> service.updateGrades(GRADE_NOK));
        assertEquals("UC does not exist", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    public void updateUCXPsOK() {
        String response = service.updateXPs(XP_OK);

        assertEquals("UC XPs successfully added", response);
    }


    @Test
    public void updateUCXPsNOK() {
        LSException thrown = assertThrows(LSException.class, () -> service.updateXPs(XP_NOK));
        assertEquals("UC does not exist", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    private void setupRepo() {
        doReturn(Optional.of(UC_NOT_STARTED)).when(repository).findById(ID_NOT_STARTED);
        doReturn(Optional.of(UC_STARTED)).when(repository).findById(ID_STARTED);
        doReturn(Optional.of(UC_NOT_ACTIVE)).when(repository).findById(ID_NOT_ACTIVE);
    }

}
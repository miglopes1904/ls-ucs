package com.learningscorecard.ucs.service;

import com.learningscorecard.ucs.exception.LSException;
import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.entity.Teacher;
import com.learningscorecard.ucs.model.entity.UC;
import com.learningscorecard.ucs.model.mapper.QuestMapperImpl;
import com.learningscorecard.ucs.model.mapper.StudentMapperImpl;
import com.learningscorecard.ucs.model.mapper.TeacherMapperImpl;
import com.learningscorecard.ucs.model.mapper.UCMapperImpl;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
import com.learningscorecard.ucs.repository.UCRepository;
import com.learningscorecard.ucs.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UCServiceTest {

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
    private final UCRepository repository = mock(UCRepository.class);
    private final UCMapperImpl mapper = new UCMapperImpl(
            new StudentMapperImpl(), new TeacherMapperImpl(), new QuestMapperImpl()
            );
    private final EntityUtils entityUtils = mock(EntityUtils.class);
    private UCService service;

    @BeforeEach
    void setUp() {
        service = new UCService(repository, mapper, entityUtils);
        setupRepo();
    }

    @Test
    public void getUCsOK() {
        List<UCDTO> response = service.getAll();

        assertEquals(2, response.size());
        assertEquals(UCDTO.class, response.get(0).getClass());
    }

    @Test
    public void getUCByIdOK() {
        UCDTO response = service.getByID(ID_1);

        assertEquals(ID_1, response.getId());
        assertEquals(UCDTO.class, response.getClass());
    }

    @Test
    public void getUCByIdNotFound() {

        LSException thrown = assertThrows(LSException.class, () -> service.getByID(ID_NOT_FOUND));

        assertEquals("UC does not exist", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    public void createUCOK() {

        String response = service.create(REQUEST_OK, AUTH_ADMIN);
        assertEquals("UC was created successfully", response);
        //verify(entityUtils).getReference(eq(Teacher.class), eq())
        verify(repository).save(any(UC.class));
    }

    @Test
    public void createUCOKTeacher() {

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

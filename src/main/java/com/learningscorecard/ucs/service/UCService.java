package com.learningscorecard.ucs.service;

import com.learningscorecard.ucs.exception.LSException;
import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.entity.Teacher;
import com.learningscorecard.ucs.model.entity.UC;
import com.learningscorecard.ucs.model.mapper.UCMapper;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
import com.learningscorecard.ucs.repository.UCRepository;
import com.learningscorecard.ucs.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UCService {

    private final UCRepository repository;
    private final UCMapper mapper;
    private final EntityUtils entityUtils;

    public UCService(UCRepository repository, UCMapper mapper, EntityUtils entityUtils) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityUtils = entityUtils;
    }

    public List<UCDTO> getAll() {
        return mapper.toDTOs(repository.findAll());
    }

    public UCDTO getByID(UUID id) {
        UC response = getOrElseThrow(id);
        return mapper.toDTO(response);
    }

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

        repository.save(uc);

        //TODO: Add UC to GraphDB

    return "UC was created successfully";
    }

    public String delete(UUID id) {
        repository.deleteById(id);
        return "UC was deleted successfully";
    }

    private UC getOrElseThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new LSException("UC does not exist", HttpStatus.BAD_REQUEST));
    }
}

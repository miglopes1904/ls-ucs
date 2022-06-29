package com.learningscorecard.ucs.model.mapper;

import com.learningscorecard.ucs.model.dto.ExtStudentDTO;
import com.learningscorecard.ucs.model.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExtStudentMapper {

    @Named("ExtStudentDTOs")
    List<ExtStudentDTO> toDTOList(List<Student> student);

    @Named("ExtStudents")
    List<Student> toStudentList(List<ExtStudentDTO> studentDTO);

}

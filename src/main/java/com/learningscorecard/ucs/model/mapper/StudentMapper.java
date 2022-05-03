package com.learningscorecard.ucs.model.mapper;

import com.learningscorecard.ucs.model.dto.StudentDTO;
import com.learningscorecard.ucs.model.entity.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    @Named("StudentDTO")
    StudentDTO toDTO(Student student);

    @Named("Student")
    Student toStudent(StudentDTO studentDTO);

    @Named("StudentDTOs")
    List<StudentDTO> toDTOList(List<Student> student);

    @Named("Students")
    List<Student> toStudentList(List<StudentDTO> studentDTO);


}

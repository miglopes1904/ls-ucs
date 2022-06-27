package com.learningscorecard.ucs.model.mapper;

import com.learningscorecard.ucs.model.dto.TeacherDTO;
import com.learningscorecard.ucs.model.dto.student.TeacherDTO4Student;
import com.learningscorecard.ucs.model.entity.Teacher;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {

    @Named("TeacherDTO")
    TeacherDTO toDTO(Teacher teacher);

    @Named("TeacherDTO4Student")
    TeacherDTO4Student toDTO4Student(Teacher teacher);

    @Named("Teacher")
    Teacher toTeacher(TeacherDTO teacherDTO);

    @Named("TeacherDTOs")
    List<TeacherDTO> toDTOs(List<Teacher> teachers);

    @Named("TeacherDTOs4Student")
    List<TeacherDTO4Student> toDTOs4Student(List<Teacher> teachers);

    @Named("Teachers")
    List<Teacher> toTeachers(List<TeacherDTO> teacherDTOs);
}

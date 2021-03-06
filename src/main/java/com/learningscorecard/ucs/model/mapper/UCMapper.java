package com.learningscorecard.ucs.model.mapper;

import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.dto.student.UCDTO4Student;
import com.learningscorecard.ucs.model.dto.teacher.UCDTO4Teacher;
import com.learningscorecard.ucs.model.entity.UC;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {StudentMapper.class, TeacherMapper.class, QuestMapper.class, GuildMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UCMapper {

    @Named("UCDTO")
    @Mapping(target = "students", source = "students", qualifiedByName = "StudentDTOs")
    @Mapping(target = "teachers", source = "teachers", qualifiedByName = "TeacherDTOs")
    @Mapping(target = "quests", source = "quests", qualifiedByName = "QuestDTOs")
    @Mapping(target = "guilds", source = "guilds", qualifiedByName = "GuildDTOs")
    UCDTO4Teacher toDTO(UC uc);

    @Named("UCDTO4Student")
    @Mapping(target = "students", source = "students", qualifiedByName = "StudentDTOs4Student")
    @Mapping(target = "teachers", source = "teachers", qualifiedByName = "TeacherDTOs4Student")
    @Mapping(target = "quests", source = "quests", qualifiedByName = "QuestDTOs")
    @Mapping(target = "guilds", source = "guilds", qualifiedByName = "GuildDTOs4Student")
    UCDTO4Student toDTO4Student(UC uc);

    @Named("UC")
    @Mapping(target = "students", source = "students", qualifiedByName = "Students")
    @Mapping(target = "teachers", source = "teachers", qualifiedByName = "Teachers")
    @Mapping(target = "quests", source = "quests", qualifiedByName = "Quests")
    @Mapping(target = "guilds", source = "guilds", qualifiedByName = "Guilds")
    UC toUC(UCDTO4Teacher ucdto);

    @Named("UCDTOs")
    @Mapping(target = "students", source = "students", qualifiedByName = "StudentDTOs")
    @Mapping(target = "teachers", source = "teachers", qualifiedByName = "TeacherDTOs")
    @Mapping(target = "quests", source = "quests", qualifiedByName = "QuestDTOs")
    @Mapping(target = "guilds", source = "guilds", qualifiedByName = "GuildDTOs")
    List<UCDTO4Teacher> toDTOs(List<UC> UCs);

    @Named("UCs")
    @Mapping(target = "students", source = "students", qualifiedByName = "Students")
    @Mapping(target = "teachers", source = "teachers", qualifiedByName = "Teachers")
    @Mapping(target = "quests", source = "quests", qualifiedByName = "Quests")
    @Mapping(target = "guilds", source = "guilds", qualifiedByName = "Guilds")
    List<UC> toUCs(List<UCDTO4Teacher> UCs);
}

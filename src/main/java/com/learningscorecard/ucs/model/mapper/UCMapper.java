package com.learningscorecard.ucs.model.mapper;

import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.entity.UC;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {StudentMapper.class, TeacherMapper.class, QuestMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UCMapper {

    @Named("UCDTO")
    @Mapping(target = "students", source = "students", qualifiedByName = "StudentDTOs")
    @Mapping(target = "teachers", source = "teachers", qualifiedByName = "TeacherDTOs")
    @Mapping(target = "quests", source = "quests", qualifiedByName = "QuestDTOs")
    UCDTO toDTO(UC uc);

    @Named("UC")
    @Mapping(target = "students", source = "students", qualifiedByName = "Students")
    @Mapping(target = "teachers", source = "teachers", qualifiedByName = "Teachers")
    @Mapping(target = "quests", source = "quests", qualifiedByName = "Quests")
    UC toUC(UCDTO ucdto);

    @Named("UCDTOs")
    @Mapping(target = "students", source = "students", qualifiedByName = "StudentDTOs")
    @Mapping(target = "teachers", source = "teachers", qualifiedByName = "TeacherDTOs")
    @Mapping(target = "quests", source = "quests", qualifiedByName = "QuestDTOs")
    List<UCDTO> toDTOs(List<UC> UCs);

    @Named("UCs")
    @Mapping(target = "students", source = "students", qualifiedByName = "Students")
    @Mapping(target = "teachers", source = "teachers", qualifiedByName = "Teachers")
    @Mapping(target = "quests", source = "quests", qualifiedByName = "Quests")
    List<UC> toUCs(List<UCDTO> UCs);
}

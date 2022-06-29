package com.learningscorecard.ucs.model.mapper;

import com.learningscorecard.ucs.model.dto.GuildDTO;
import com.learningscorecard.ucs.model.entity.Guild;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {ExtStudentMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GuildMapper {

    @Mapping(target = "students", source = "students", qualifiedByName = "ExtStudentDTOs")
    GuildDTO toDTO(Guild guild);

    @Mapping(target = "students", source = "students", qualifiedByName = "ExtStudents")
    Guild toGuild(GuildDTO guild);

    @Named("GuildDTOs")
    @Mapping(target = "students", source = "students", qualifiedByName = "ExtStudentDTOs")
    List<GuildDTO> toDTOs(List<Guild> guild);

    @Named("Guilds")
    @Mapping(target = "students", source = "students", qualifiedByName = "ExtStudents")
    List<Guild> toGuilds(List<GuildDTO> guild);
}

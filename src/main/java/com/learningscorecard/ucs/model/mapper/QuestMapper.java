package com.learningscorecard.ucs.model.mapper;

import com.learningscorecard.ucs.model.dto.QuestDTO;
import com.learningscorecard.ucs.model.entity.Quest;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestMapper {

    @Named("QuestDTO")
    QuestDTO toDTO(Quest quest);

    @Named("Quest")
    Quest toQuest(QuestDTO studentDTO);

    @Named("QuestDTOs")
    List<QuestDTO> toDTOList(List<Quest> student);

    @Named("Quests")
    List<Quest> toQuestList(List<QuestDTO> studentDTO);
}

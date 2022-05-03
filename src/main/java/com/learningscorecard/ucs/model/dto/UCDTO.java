package com.learningscorecard.ucs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learningscorecard.ucs.model.entity.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UCDTO {

    private UUID id;

    private String name;

    private String acronym;

    @Builder.Default
    private Boolean active = Boolean.TRUE;

    private Boolean started;

    @JsonProperty("academic_year")
    private String academicYear;

    private Integer semester;

    @Builder.Default
    private List<StudentDTO> students = new ArrayList<>();

    @Builder.Default
    private List<TeacherDTO> teachers = new ArrayList<>();

    @Builder.Default
    private List<QuestDTO> quests = new ArrayList<>();

    @Builder.Default
    private List<String> alliances = new ArrayList<>();

    @Builder.Default
    private List<Rank> ranks = new ArrayList<>();

    @Builder.Default
    private List<Grade> grades = new ArrayList<>();

    @Builder.Default
    private List<XP> XPs = new ArrayList<>();

}

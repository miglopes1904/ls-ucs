package com.learningscorecard.ucs.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learningscorecard.ucs.model.entity.Alliance;
import com.learningscorecard.ucs.model.entity.Difficulty;
import com.learningscorecard.ucs.model.entity.Progress;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO extends UserDTO {

    private Long number;

    @Builder.Default
    @JsonProperty("continuous_assessment")
    private Boolean continuousAssessment = Boolean.TRUE;

    @Builder.Default
    private List<Alliance> alliances = new ArrayList<>();

    @Builder.Default
    private List<Progress> progresses = new ArrayList<>();

    @Builder.Default
    private List<Difficulty> difficulties = new ArrayList<>();
}

package com.learningscorecard.ucs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuildDTO {

    private UUID id;

    private String name;

    @JsonProperty("project_title")
    private String projectTitle;

    @JsonProperty("project_description")
    private String projectDescription;

    private List<ExtStudentDTO> students;

    private UUID uc;

}
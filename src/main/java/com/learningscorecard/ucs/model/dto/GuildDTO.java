package com.learningscorecard.ucs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learningscorecard.ucs.model.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @JsonProperty("created_at")
    private LocalDate createdAt;

    private String alliance;

    @Builder.Default
    private List<Feedback> feedbacks = new ArrayList<>();

    @JsonProperty("project_description")
    private String projectDescription;

    private List<ExtStudentDTO> students = new ArrayList<>();

    private UUID uc;

}

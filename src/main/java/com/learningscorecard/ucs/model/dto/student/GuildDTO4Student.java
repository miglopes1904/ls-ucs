package com.learningscorecard.ucs.model.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learningscorecard.ucs.model.dto.ExtStudentDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuildDTO4Student {

    private UUID id;

    private String name;

    @JsonProperty("project_title")
    private String projectTitle;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    private String alliance;

    @JsonProperty("project_description")
    private String projectDescription;

    private List<ExtStudentDTO4Student> students;

    private UUID uc;
}

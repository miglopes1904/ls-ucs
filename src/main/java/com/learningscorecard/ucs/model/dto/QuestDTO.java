package com.learningscorecard.ucs.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestDTO {

    private UUID id;

    private Integer week;

    private String type;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("validation_date")
    private Date validationDate;

    @JsonProperty("last_chance_date")
    private Date lastChanceDate;

    private String title;

    private String summary;

    private String description;

    private String support;

    @Builder.Default
    private Boolean mandatory = Boolean.FALSE;

    @Builder.Default
    @JsonProperty("lecture_type")
    private String lectureType = null;

    @Builder.Default
    @JsonProperty("max_xps")
    private Long maxXps = 0L;
}

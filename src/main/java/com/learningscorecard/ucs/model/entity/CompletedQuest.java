package com.learningscorecard.ucs.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompletedQuest {

    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Builder.Default
    private Long value = 0L;

    @Builder.Default
    private Boolean validated = Boolean.FALSE;

    private String type;

    private String title;

    @Column(name = "start_date")
    @JsonProperty("start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonProperty("end_date")
    private LocalDate endDate;

    @Column(name = "last_chance_date")
    @JsonProperty("last_chance_date")
    private LocalDate lastChanceDate;

    @Column(name = "validation_date")
    @JsonProperty("validation_date")
    private LocalDate validationDate;

    @Builder.Default
    private LocalDate timestamp = LocalDate.now();

    private String grade;

    private Integer difficulty;
}

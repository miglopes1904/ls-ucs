package com.learningscorecard.ucs.model.entity;

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

    private UUID id;

    @Builder.Default
    private Long value = 0L;

    @Builder.Default
    private Boolean validated = Boolean.FALSE;

    private String type;

    private String title;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "last_chance_date")
    private LocalDate lastChanceDate;

    @Builder.Default
    private LocalDate timestamp = LocalDate.now();

    private String grade;

    private Integer difficulty;
}

package com.learningscorecard.ucs.model.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlanningEntry {

    private String title;
    private String cardTitle;
    private String cardSubtitle;
    private Integer week;
    private LocalDate date;
}

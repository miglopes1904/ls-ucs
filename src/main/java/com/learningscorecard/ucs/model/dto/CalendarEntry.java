package com.learningscorecard.ucs.model.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalendarEntry {

    private String title;
    private LocalDate date;
}

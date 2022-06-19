package com.learningscorecard.ucs.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JournalEntry {

    private UUID id;
    private LocalDate date;
    private String title;
    @Builder.Default
    private List<JournalStudent> students = new ArrayList<>();

}

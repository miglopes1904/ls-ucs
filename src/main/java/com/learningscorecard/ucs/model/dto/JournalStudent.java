package com.learningscorecard.ucs.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JournalStudent {
    private String username;
    private Boolean validated;
    private Long XPs;
    private String grade;
    private Integer difficulty;
}

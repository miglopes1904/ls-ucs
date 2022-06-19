package com.learningscorecard.ucs.model.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaderboardEntry {

    private UUID id;
    private String name;
    private String alliance;
    private Long XP;
    private String title;
    private String avatar;
}

package com.learningscorecard.ucs.model.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaderboardEntry {

    private int index;
    private String name;
    private String alliance;
    @Builder.Default
    private Long XP = 0L;
    @Builder.Default
    private String title = "Newbie";
    private String avatar;
}

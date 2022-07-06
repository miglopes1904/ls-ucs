package com.learningscorecard.ucs.model.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DifficultyEntry {

    private UUID content;
    private List<Integer> difficulties;
    private Integer total;
    private Integer mean;
}

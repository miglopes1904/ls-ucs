package com.learningscorecard.ucs.model.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentDifficulty {

    private UUID id;
    private String title;
    @Builder.Default
    @Min(-1)
    @Max(5)
    private Integer value = -1;
}

package com.learningscorecard.ucs.model.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GradeValue {

    private String name;

    @Min(0)
    @Max(100)
    private Integer value;
}

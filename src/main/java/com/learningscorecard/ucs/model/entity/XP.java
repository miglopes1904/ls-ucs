package com.learningscorecard.ucs.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class XP {

    private String name;

    private Long value;

    @Column(name = "last_chance_value")
    @JsonProperty("last_chance_value")
    @Min(0)
    @Max(100)
    private Integer lastChanceValue;
}

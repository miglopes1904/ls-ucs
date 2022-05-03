package com.learningscorecard.ucs.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class XP {

    private String name;

    private Long value;

    @Column(name = "last_chance_value")
    @JsonProperty("last_chance_value")
    private Integer lastChanceValue;
}

package com.learningscorecard.ucs.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUCRequest {

    private String name;

    private String acronym;

    private int semester;

    @JsonProperty("academic_year")
    private String academicYear;

    private List<String> alliances;
}

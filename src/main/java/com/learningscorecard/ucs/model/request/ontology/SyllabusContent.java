package com.learningscorecard.ucs.model.request.ontology;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SyllabusContent {

    private UUID id;
    private String title;
    private Integer level;
    @Builder.Default
    private List<UUID> includes = new ArrayList<>();
    @Builder.Default
    private String partOf = "0";
}

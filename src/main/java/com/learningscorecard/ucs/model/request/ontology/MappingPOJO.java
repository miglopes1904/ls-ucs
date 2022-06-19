package com.learningscorecard.ucs.model.request.ontology;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MappingPOJO {

    private UUID id;
    private String title;
}

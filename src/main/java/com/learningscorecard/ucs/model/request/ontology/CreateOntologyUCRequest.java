package com.learningscorecard.ucs.model.request.ontology;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOntologyUCRequest {

    private UUID id;

    private String name;
}

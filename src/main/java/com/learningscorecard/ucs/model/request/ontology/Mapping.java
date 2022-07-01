package com.learningscorecard.ucs.model.request.ontology;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mapping {

    private UUID id;
    private String title;
    private List<MappingPOJO> covered;
}

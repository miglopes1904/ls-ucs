package com.learningscorecard.ucs.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Alliance {

    private String name;

    @Column(columnDefinition = "BINARY(16)")
    private UUID uc;

}

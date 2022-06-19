package com.learningscorecard.ucs.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Embeddable
@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Avatar {

    private String value;
    private UUID uc;

    public Avatar(String value) {
        this.value = value;
    }

}

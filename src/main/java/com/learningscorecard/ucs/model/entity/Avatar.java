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

    @Column(name = "value")
    @Enumerated(EnumType.STRING)
    private AvatarValue value;
    private UUID uc;

    public Avatar(AvatarValue value) {
        this.value = value;
    }


    public enum AvatarValue {
        USER,
        EAGLE,
        BEAR
    }
}

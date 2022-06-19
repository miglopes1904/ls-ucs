package com.learningscorecard.ucs.model.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {

    public static final String NEWBIE = "Newbie";

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private UUID uc;

    @Builder.Default
    private String rank = NEWBIE;

    @Builder.Default
    private Long value = 0L;

    @ElementCollection
    @Builder.Default
    private List<CompletedQuest> completedQuests = new ArrayList<>();

}

package com.learningscorecard.ucs.model.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "progress")
@Data
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

    private String rank = NEWBIE;

    private Long value = 0L;

    @OneToMany
    @JoinColumn(name = "progress_id")
    private Set<CompletedQuest> completedQuests;

}

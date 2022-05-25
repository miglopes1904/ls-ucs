package com.learningscorecard.ucs.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "completed_quests")
@Builder
@Getter
@Setter
public class CompletedQuest {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    private Quest quest;

    @Builder.Default
    private Long value = 0L;

    private String grade;

    @Builder.Default
    private Boolean validated = Boolean.FALSE;

    private Integer difficulty;

    private String type;

    private String title;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "last_chance_date")
    private LocalDate lastChanceDate;

    @Builder.Default
    private LocalDate timestamp = LocalDate.now();
}

package com.learningscorecard.ucs.model.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(columnDefinition = "BINARY(16)")
    private UUID uc;

    @Builder.Default
    private String rank = NEWBIE;

    @Builder.Default
    private Long value = 0L;

    @ElementCollection
    @Builder.Default
    private List<CompletedQuest> completedQuests = new ArrayList<>();

}

package com.learningscorecard.ucs.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {

    //student
    @Column(name = "number")
    private Long number;
    //student
    @Builder.Default
    @Column(name = "continuous_assessment")
    private Boolean continuousAssessment = Boolean.TRUE;
    //student

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = {/*CascadeType.PERSIST, */CascadeType.DETACH})
    @JoinTable(
            name = "student_UC",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "UC_id"))
    private List<UC> UCs = new ArrayList<>();
    //TODO: Add progress


    @ElementCollection
    @Builder.Default
    private List<Alliance> alliances = new ArrayList<>();
/*
    @Builder.Default
    private String type = STUDENT;*/

    //student
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "student_guild",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "guild_id"))
    private List<Guild> guilds = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private List<Progress> progresses = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private List<Difficulty> difficulties = new ArrayList<>();

}

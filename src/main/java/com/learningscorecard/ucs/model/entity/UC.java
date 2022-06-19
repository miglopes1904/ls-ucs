package com.learningscorecard.ucs.model.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "UCs")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UC {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String name;

    private String acronym;

    @Builder.Default
    private Boolean active = Boolean.TRUE;

    @Builder.Default
    private Boolean started = Boolean.FALSE;

    @Column(name = "academic_year")
    private String academicYear;

    private Integer semester;

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = {/*CascadeType.PERSIST,*/ CascadeType.DETACH})
    @JoinTable(
            name = "student_UC",
            joinColumns = @JoinColumn(name = "UC_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Student> students = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = {/*CascadeType.PERSIST,*/ CascadeType.DETACH})
    @JoinTable(
            name = "teacher_UC",
            joinColumns = @JoinColumn(name = "UC_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Teacher> teachers = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "ownerUC")
    private List<Quest> quests = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "ownerUC")
    private List<Guild> guilds = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<String> alliances = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<Rank> ranks = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    private List<GradeValue> gradeValues = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<XP> XPs = new ArrayList<>();

}

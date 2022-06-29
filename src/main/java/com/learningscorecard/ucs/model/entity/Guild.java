package com.learningscorecard.ucs.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "guilds")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Guild {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    @Column(name = "project_title")
    private String projectTitle;

    @Column(name = "project_description")
    private String projectDescription;

    @Builder.Default
    @ElementCollection
    private List<Feedback> feedbacks = new ArrayList<>();

    private String alliance;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "uc_id")
    private UC ownerUC;

    @Column(name = "uc_id", insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uc;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "student_guild",
            joinColumns = @JoinColumn(name = "guild_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students = new ArrayList<>();
}

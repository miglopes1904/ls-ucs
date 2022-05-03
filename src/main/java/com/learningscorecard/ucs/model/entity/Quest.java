package com.learningscorecard.ucs.model.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "quests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Quest {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private Integer week;

    private String type;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "validation_date")
    private Date validationDate;

    @Column(name = "last_chance_date")
    private Date lastChanceDate;

    private String title;

    private String summary;

    private String description;

    private String support;

    @Builder.Default
    private Boolean mandatory = Boolean.FALSE;

    @Builder.Default
    @Column(name = "lecture_type")
    private String lectureType = null;

    @Builder.Default
    @Column(name = "max_xps")
    private Long maxXps = 0L;

    @ManyToOne
    @JoinColumn(name = "uc_id")
    private UC ownerUC;

    @Column(name = "uc_id", insertable = false, updatable = false)
    private UUID uc;
}

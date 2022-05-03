package com.learningscorecard.ucs.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("TEACHER")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends User {

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = {/*CascadeType.PERSIST, */CascadeType.DETACH})
    @JoinTable(
            name = "teacher_UC",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "UC_id"))
    private List<UC> UCs = new ArrayList<>();
    //TODO: Add progress

    /*@Builder.Default
    private String type = "TEACHER";*/
}

package com.learningscorecard.ucs.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type",
        discriminatorType = DiscriminatorType.STRING)
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class User {

    public static final String TO_BE_DEFINED = "To be defined";
    //User related
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Builder.Default
    @Column(name = "username")
    private String username = TO_BE_DEFINED;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    //LS related
    @Column(name = "name")
    private String name;

    @Builder.Default
    @Column(name = "requested_reset")
    private Boolean requestedReset = Boolean.FALSE;

    @Column(name = "activation_token")
    private String activationToken;

    @ElementCollection
    @CollectionTable(
            name = "avatars",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private List<Avatar> avatars = new ArrayList<>();

    @Column(name = "last_login")
    private LocalDate lastLogin;

    @Builder.Default
    private Boolean activated = Boolean.FALSE;

    @Column(name = "user_type", updatable = false, insertable = false)
    private String type;

}

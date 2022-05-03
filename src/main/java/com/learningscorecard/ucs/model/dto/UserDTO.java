package com.learningscorecard.ucs.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learningscorecard.ucs.model.entity.Avatar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@ToString
@JsonInclude(Include.NON_NULL)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;

    private String username;

    private String email;

    private List<Avatar> avatars;

    @JsonProperty("last_login")
    private LocalDate lastLogin;

    private Boolean activated;

    private String type;
}

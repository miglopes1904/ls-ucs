package com.learningscorecard.ucs.model.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.learningscorecard.ucs.model.entity.Alliance;
import com.learningscorecard.ucs.model.entity.Avatar;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtStudentDTO4Student {

    private UUID id;

    private String username;

    private Long number;

    private String email;

    private List<Avatar> avatars;

    private List<Alliance> alliances;
}

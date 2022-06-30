package com.learningscorecard.ucs.model.dto;

import com.learningscorecard.ucs.model.entity.Alliance;
import com.learningscorecard.ucs.model.entity.Avatar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtStudentDTO {

    private UUID id;

    private String username;

    private String email;

    private List<Avatar> avatars;

    private List<Alliance> alliances;

}

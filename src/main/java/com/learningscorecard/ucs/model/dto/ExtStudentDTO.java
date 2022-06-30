package com.learningscorecard.ucs.model.dto;

import com.learningscorecard.ucs.model.entity.Alliance;
import com.learningscorecard.ucs.model.entity.Avatar;
import com.learningscorecard.ucs.model.entity.Progress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtStudentDTO {

    private UUID id;

    private String username;

    private Long number;

    private String email;

    @Builder.Default
    private List<Avatar> avatars = new ArrayList<>();

    @Builder.Default
    private List<Alliance> alliances = new ArrayList<>();

    @Builder.Default
    private List<Progress> progresses = new ArrayList<>();

}

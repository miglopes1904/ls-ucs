package com.learningscorecard.ucs.model.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.learningscorecard.ucs.model.entity.Avatar;
import lombok.*;

import java.util.List;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO4Student {

    private String name;

    private String username;

    private String email;

    private List<Avatar> avatars;
}

package com.learningscorecard.ucs.model.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.learningscorecard.ucs.model.dto.TeacherDTO;
import com.learningscorecard.ucs.model.dto.UCDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UCDTO4Student extends UCDTO {

    @Builder.Default
    private List<StudentDTO4Student> students = new ArrayList<>();

    @Builder.Default
    private List<TeacherDTO4Student> teachers = new ArrayList<>();

}

package com.learningscorecard.ucs.model.dto.teacher;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.learningscorecard.ucs.model.dto.GuildDTO;
import com.learningscorecard.ucs.model.dto.StudentDTO;
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
public class UCDTO4Teacher extends UCDTO {

    @Builder.Default
    private List<StudentDTO> students = new ArrayList<>();

    @Builder.Default
    private List<TeacherDTO> teachers = new ArrayList<>();

    @Builder.Default
    private List<GuildDTO> guilds = new ArrayList<>();
}

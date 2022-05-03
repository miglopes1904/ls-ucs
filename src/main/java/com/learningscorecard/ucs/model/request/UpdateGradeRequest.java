package com.learningscorecard.ucs.model.request;

import com.learningscorecard.ucs.model.entity.Grade;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateGradeRequest {

    private UUID id;

    private List<Grade> grades;
}

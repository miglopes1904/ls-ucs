package com.learningscorecard.ucs.model.request;

import com.learningscorecard.ucs.model.entity.XP;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateXPRequest {

    private UUID id;

    private List<XP> XPs;
}

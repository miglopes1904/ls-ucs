package com.learningscorecard.ucs.model.request;

import com.learningscorecard.ucs.model.entity.Rank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateRankRequest {

    private UUID id;

    private List<Rank> ranks;
}

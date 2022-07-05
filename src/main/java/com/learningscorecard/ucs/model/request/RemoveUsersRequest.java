package com.learningscorecard.ucs.model.request;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemoveUsersRequest {

    private List<UUID> students;

    private UUID uc;
}

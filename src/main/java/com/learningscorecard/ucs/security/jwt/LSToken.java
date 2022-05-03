package com.learningscorecard.ucs.security.jwt;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LSToken {

    private UUID id;

    private String reason;

    public LSToken(UUID id, String reason) {
        this.id = id;
        this.reason = reason;
    }
}

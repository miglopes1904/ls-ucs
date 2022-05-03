package com.learningscorecard.ucs.security.config;

import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ls.auth.security")
@Getter
@Setter
public class JWTConfig {

    public static final String BEARER = "Bearer ";

    private String secret;

    private String expirationMs;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}

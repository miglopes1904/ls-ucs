package com.learningscorecard.ucs.security.jwt;

import com.google.common.collect.Sets;
import com.learningscorecard.ucs.exception.LSException;
import com.learningscorecard.ucs.util.MessageConstants;
import com.learningscorecard.ucs.util.MessageUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Getter
public class JwtUtils {

    public static final String BEARER = "Bearer ";
    public static final String PASSWORD_RESET = "Password Reset";
    private static final String ROLE_ = "ROLE_";
    private final String secret;

    public JwtUtils(@Value("${ls.auth.security.secret}") String secret) {
        this.secret = secret;
    }

    public String generate(UUID id, String type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", Sets.newHashSet(new SimpleGrantedAuthority(ROLE_ + type)));
        claims.put("type", type);
        return Jwts.builder()
                .setSubject(id.toString())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(2).toInstant()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public UUID parse(String token) {
        UUID id;
        String parsedToken = token.replace(BEARER, "");

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(parsedToken);

            Claims body = claimsJws.getBody();
            id = UUID.fromString(body.getSubject());
        } catch (RuntimeException e) {
            throw new LSException(MessageUtils.getMessage(MessageConstants.BAD_TOKEN), HttpStatus.UNAUTHORIZED);
        }

        return id;
    }
}

package com.learningscorecard.ucs.security.filter;

import com.google.common.base.Strings;
import com.learningscorecard.ucs.security.config.JWTConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JWTVerificationFilter extends OncePerRequestFilter {

    private final JWTConfig jwtConfig;

    public JWTVerificationFilter(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(JWTConfig.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(JWTConfig.BEARER, "");

        try {

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                    .build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            var authorities = (List<Map<String, String>>) body.get("authorities");
            //String type = (String) body.get("type");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            /*Set<SimpleGrantedAuthority> simpleGrantedAuthorities =
                    Sets.newHashSet(new SimpleGrantedAuthority(type));
*/
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    body.getSubject(),
                    authorizationHeader,
                    simpleGrantedAuthorities
            );
            //authentication.setDetails(type);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException e) {
            //throw new LSException("Token cannot be trusted");
        }

        filterChain.doFilter(request, response);
    }
}

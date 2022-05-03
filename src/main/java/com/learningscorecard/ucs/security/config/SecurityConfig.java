package com.learningscorecard.ucs.security.config;

import com.learningscorecard.ucs.security.filter.AuthEntryPointJWT;
import com.learningscorecard.ucs.security.filter.JWTVerificationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthEntryPointJWT unauthorizedHandler;
    private final JWTConfig jwtConfig;

    public SecurityConfig(AuthEntryPointJWT unauthorizedHandler, JWTConfig jwtConfig) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterAfter(new JWTVerificationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/login", "/complete/{id}", "/cancel/{token}",
                        "/reset-password", "/confirm-reset", "/cancel-reset/{token}").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }


}

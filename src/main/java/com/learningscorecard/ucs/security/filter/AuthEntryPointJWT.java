package com.learningscorecard.ucs.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learningscorecard.ucs.exception.ExceptionResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@Component
@Slf4j
public class AuthEntryPointJWT implements AuthenticationEntryPoint {

    public static final String EXCEPTION_THROWN_MESSAGE_CAUSE =
            "\n**** Exception thrown -> {} \n**** Message: {} \n**** Cause: {} ";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ExceptionResponseBody exceptionResponseBody = ExceptionResponseBody
                .builder()
                .message("Unauthorized")
                .timestamp(new Date())
                .build();

        log.error(EXCEPTION_THROWN_MESSAGE_CAUSE,
                authException.getClass().getSimpleName(),
                authException.getMessage(),
                authException.getStackTrace()[0]);

        response.setContentType("application/json");
        response.setStatus(401);
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, exceptionResponseBody);
        out.flush();
    }
}

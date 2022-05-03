package com.learningscorecard.ucs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learningscorecard.ucs.exception.ExceptionResponseBody;
import com.learningscorecard.ucs.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityChecks {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    JwtUtils jwtUtils;



    @Test
    public void isUnauthorizedNoToken() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertEquals("Unauthorized",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    @Test
    public void UCServiceBadRole() throws Exception {
        MvcResult result = mockMvc.perform(get("/")
                        .header("Authorization", "Bearer " + jwtUtils.generate(UUID.randomUUID(), "STUDENT")))
                .andExpect(status().isForbidden())
                .andReturn();

        assertEquals("Access denied",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

    @Test
    public void managementServiceBadRole() throws Exception {
        MvcResult result = mockMvc.perform(put("/start/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + jwtUtils.generate(UUID.randomUUID(), "STUDENT")))
                .andExpect(status().isForbidden())
                .andReturn();

        assertEquals("Access denied",
                mapper.readValue(result.getResponse().getContentAsString(),
                        ExceptionResponseBody.class).getMessage());
    }

}

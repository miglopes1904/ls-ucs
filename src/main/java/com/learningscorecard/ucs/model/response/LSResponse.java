package com.learningscorecard.ucs.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
public class LSResponse<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private T data;

    public LSResponse(T data) {
        this.data = data;
    }
}


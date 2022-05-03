package com.learningscorecard.ucs.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class LSResponse<T> {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private T data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS")
    private final Date timestamp = new Date();

    public LSResponse(T data) {
        this.data = data;
    }
}


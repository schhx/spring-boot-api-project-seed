package com.company.project.exception.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorVO {

    private String error;

    @JsonProperty("original_error")
    private String originalError;

    private ErrorVO(String error, String originalError) {
        this.error = error;
        this.originalError = originalError;
    }

    public static ErrorVO of(String error) {
        return new ErrorVO(error, error);
    }

    public static ErrorVO of(String error, String originalError) {
        return new ErrorVO(error, originalError);
    }

}

// src/main/java/com/starboost/starboost_backend_demo/exception/ApiError.java
package com.starboost.starboost_backend_demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class ApiError {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;

    // no-args for Jackson
    public ApiError() {}

    // 5-arg constructor (no path)
    public ApiError(Instant timestamp,
                    int status,
                    String error,
                    String message,
                    List<FieldError> fieldErrors) {
        this.timestamp     = timestamp;
        this.status        = status;
        this.error         = error;
        this.message       = message;
        this.fieldErrors   = fieldErrors;
    }

    // 6-arg constructor (with path)
    public ApiError(Instant timestamp,
                    int status,
                    String error,
                    String message,
                    String path,
                    List<FieldError> fieldErrors) {
        this(timestamp, status, error, message, fieldErrors);
        this.path = path;
    }

    @Data @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String rejectedValue;
        private String message;
    }
}

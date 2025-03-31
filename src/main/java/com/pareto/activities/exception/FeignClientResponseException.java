package com.pareto.activities.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class FeignClientResponseException extends RuntimeException {

    private String title;
    private String timestamp;
    private String error;
    private int status;
    private String message;
    private String path;
    private String key;
    private String code;
    private String httpStatus;

    public FeignClientResponseException(ExceptionMessage exceptionMessage) {
        this.code = exceptionMessage.getCode();
        this.key = exceptionMessage.getKey();
        this.title = exceptionMessage.getTitle();
        this.path = exceptionMessage.getPath();
        this.message = exceptionMessage.getMessage();
        this.error = exceptionMessage.getError();
        this.timestamp = exceptionMessage.getTimestamp();
        this.status = exceptionMessage.getStatus();
        this.httpStatus = exceptionMessage.getHttpStatus();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExceptionMessage {
        private String title;
        private String timestamp;
        private String error;
        private int status;
        private String message;
        private String path;
        private String key;
        private String code;
        private String httpStatus;
    }
}

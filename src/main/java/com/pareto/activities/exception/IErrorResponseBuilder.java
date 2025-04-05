package com.pareto.activities.exception;

import com.pareto.activities.exception.utils.ExceptionKeyAndMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

public interface IErrorResponseBuilder {
    ResponseEntity<Object> ErrorResponseBuilder(
            WebRequest request,
            HttpStatus httpStatus,
            Exception ex,
            Enum<? extends ExceptionKeyAndMessage> globalExceptionCodes
    );

    ResponseEntity<Object> ErrorResponseBuilder(
            WebRequest request,
            HttpStatus httpStatus,
            String techMessage,
            int code,
            List<?> errors
    );

    ResponseEntity<Object> ErrorResponseBuilder(
            WebRequest request,
            HttpStatus httpStatus,
            String techMessage,
            int code,
            String businessMessage,
            List<?> validationErrors
    );

    ResponseEntity<Object> ErrorResponseBuilder(
            WebRequest request,
            HttpStatus httpStatus,
            String techMessage,
            int code,
            String businessMessage,
            Map<String, ?> validationErrors
    );
}

package com.pareto.activities.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pareto.activities.config.Constant;
import com.pareto.activities.enums.GeneralExceptionMessages;
import com.pareto.activities.exception.utils.ExceptionKeyAndMessage;
import com.pareto.activities.exception.utils.HttpResponseConstants;
import com.pareto.activities.exception.utils.TechExceptionCodes;
import com.pareto.activities.exception.utils.ValidationError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.ConnectException;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.security.KeyStoreException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INVALID_REQUEST_FORMAT = "Invalid request data. Please check your request body.";

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handle(
            GeneralException ex,
            WebRequest request
    ) {
        log.error(
                "General Exception {}",
                ex.getMessage()
        );
        return ofType(
                request,
                ex.getHttpStatus(),
                ex.getMessage(),
                ex.getMessage(),
                ex.getCode(),
                Collections.emptyList()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handle(
            RuntimeException ex,
            WebRequest request
    ) {
        log.error(
                "RuntimeException: {}",
                ex.getMessage()
        );
        return ofType(
                request,
                HttpStatus.BAD_REQUEST,
                TechExceptionCodes.RUNTIME_EXCEPTION.getExceptionMessage(),
                TechExceptionCodes.RUNTIME_EXCEPTION.getExceptionCode(),
                Collections.emptyList()
        );
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handle(
            AccessDeniedException ex,
            WebRequest request
    ) {
        log.error(
                "Access denied: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.ACCESS_DENIED,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Object> handle(
            ConnectException ex,
            WebRequest request
    ) {
        log.error(
                "Connect Exception: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.CONNECT_EXCEPTION,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(FeignClientResponseException.class)
    public ResponseEntity<Object> handle(
            FeignClientResponseException ex,
            WebRequest request
    ) {
        log.error(
                "FeignClientResponseException: {}",
                ex.getMessage()
        );
        if ("2009".equals(ex.getCode()) || "2006".equals(ex.getCode())) {
            return ofType(
                    request,
                    HttpStatus.NOT_FOUND,
                    "(" + TechExceptionCodes.FEIGN_CLIENT + ")" + "Card not found. Please check input details",
                    "Card not found. Please check input details",
                    TechExceptionCodes.FEIGN_CLIENT.getExceptionCode(),
                    Collections.emptyList()
            );
        }
        else {
            return ofType(
                    request,
                    HttpStatus.valueOf(ex.getStatus()),
                    "(" + TechExceptionCodes.FEIGN_CLIENT.getExceptionMessage() + ")" + ex.getMessage(),
                    ex.getMessage(),
                    TechExceptionCodes.FEIGN_CLIENT.getExceptionCode(),
                    Collections.emptyList()
            );
        }

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handle(
            MethodArgumentTypeMismatchException ex,
            WebRequest request
    ) {
        log.error(
                "MethodArgumentTypeMismatchException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.METHOD_ARGUMENT_TYPE_MISMATCH,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handle(
            NoSuchElementException ex,
            WebRequest request
    ) {
        log.error(
                "NoSuchElementException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.NO_SUCH_ELEMENT,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(KeyStoreException.class)
    public ResponseEntity<Object> handle(
            KeyStoreException ex,
            WebRequest request
    ) {
        log.error(
                "KeyStoreException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.KEY_STORE_EXCEPTION,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(SocketException.class)
    public ResponseEntity<Object> handle(
            SocketException ex,
            WebRequest request
    ) {
        log.error(
                "SocketException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.SOCKET_EXCEPTION,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handle(
            DateTimeParseException ex,
            WebRequest request
    ) {
        log.error(
                "DateTimeParseException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.DATE_TIME_PARSE,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handle(
            InvalidFormatException ex,
            WebRequest request
    ) {
        log.error(
                "InvalidFormatException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.INVALID_FORMAT,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoSuchBeanDefinitionException.class)
    public ResponseEntity<Object> handle(
            NoSuchBeanDefinitionException ex,
            WebRequest request
    ) {
        log.error(
                "NoSuchBeanDefinitionException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.NO_SUCH_BEAN_DEFINITION,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handle(
            NullPointerException ex,
            WebRequest request
    ) {
        log.error(
                "NullPointerException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.NULL_POINTER,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handle(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        log.error(
                "IllegalArgumentException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.ILLEGAL_ARGUMENT,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Object> handle(
            ConversionFailedException ex,
            WebRequest request
    ) {
        log.error(
                "ConversionFailedException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.CONVERSION_EXCEPTION,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Object> handle(
            UnexpectedTypeException ex,
            WebRequest request
    ) {
        log.error(
                "UnexpectedTypeException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.UNEXPECTED_TYPE,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Object> handle(
            ArithmeticException ex,
            WebRequest request
    ) {
        log.error(
                "ArithmeticException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.ARITHMETIC,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<Object> handle(
            ClassCastException ex,
            WebRequest request
    ) {
        log.error(
                "ClassCastException: {}",
                ex.getMessage()
        );
        return ofType(
                ex,
                request,
                TechExceptionCodes.CLASS_CAST_EXCEPTION,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handle(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        List<ValidationError> validationErrors = constraintViolations
                .stream()
                .map(p -> ValidationError
                        .builder()
                        .field(p
                                       .getPropertyPath()
                                       .toString())
                        .rejectedValue(Objects
                                               .requireNonNull(p.getInvalidValue())
                                               .toString())
                        .rejectedMessage(p.getMessage())
                        .build())
                .collect(Collectors.toList())
                ;
        log.error(
                "javax.validation.ConstraintViolationException: {}",
                ex.getMessage()
        );
        return ofType(
                request,
                HttpStatus.BAD_REQUEST,
                TechExceptionCodes.CONSTRAINT_VIOLATION.getExceptionMessage(),
                TechExceptionCodes.CONSTRAINT_VIOLATION.getExceptionCode(),
                validationErrors
        );
    }

    private ResponseEntity<Object> ofType(
            Exception ex,
            WebRequest request,
            Enum<? extends ExceptionKeyAndMessage> globalExceptionCodes,
            HttpStatus httpStatus
    ) {
        if (ex instanceof HttpMessageNotReadableException) {
            return ofType(
                    request,
                    httpStatus,
                    TechExceptionCodes.INVALID_REQUEST_EXCEPTION.getExceptionMessage(),
                    INVALID_REQUEST_FORMAT,
                    ((ExceptionKeyAndMessage) globalExceptionCodes).getExceptionCode(),
                    Collections.emptyList()
            );
        }
        else {
            return ofType(
                    request,
                    httpStatus,
                    ((ExceptionKeyAndMessage) globalExceptionCodes).getExceptionMessage() + "(" + ex.getMessage() + ")",
                    ((ExceptionKeyAndMessage) globalExceptionCodes).getExceptionCode(),
                    Collections.emptyList()
            );
        }
    }

    private ResponseEntity<Object> ofType(
            WebRequest request,
            HttpStatus httpStatus,
            String techMessage,
            int code,
            List<?> errors
    ) {

        String exceptionMessage = GeneralExceptionMessages.getExceptionByCode(Constant.AZ);

        return ofType(
                request,
                httpStatus,
                techMessage,
                exceptionMessage,
                code,
                errors
        );
    }

    private ResponseEntity<Object> ofType(
            WebRequest request,
            HttpStatus status,
            String techMessage,
            String businessMessage,
            int code,
            List<?> validationErrors
    ) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(
                HttpResponseConstants.CODE,
                code
        );
        attributes.put(
                HttpResponseConstants.MESSAGE,
                businessMessage.replace(
                        "\\n",
                        "\n"
                )
        );
        if (Objects.nonNull(techMessage)) {
            attributes.put(
                    HttpResponseConstants.TECH_MESSAGE,
                    techMessage
            );
        }
        attributes.put(
                HttpResponseConstants.STATUS,
                status.value()
        );
        attributes.put(
                HttpResponseConstants.ERROR,
                status.getReasonPhrase()
        );
        if (!validationErrors.isEmpty()) {
            attributes.put(
                    HttpResponseConstants.VALIDATION_ERRORS,
                    validationErrors
            );
        }
        attributes.put(
                HttpResponseConstants.TIMESTAMP,
                LocalDateTime
                        .now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        attributes.put(
                HttpResponseConstants.PATH,
                ((ServletWebRequest) request)
                        .getRequest()
                        .getRequestURI()
        );
        return new ResponseEntity<>(
                attributes,
                status
        );
    }
}

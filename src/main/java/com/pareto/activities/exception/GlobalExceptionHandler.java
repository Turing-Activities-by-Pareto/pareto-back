package com.pareto.activities.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pareto.activities.config.Constants;
import com.pareto.activities.enums.GeneralExceptionMessages;
import com.pareto.activities.exception.utils.ExceptionKeyAndMessage;
import com.pareto.activities.exception.utils.HttpResponseConstants;
import com.pareto.activities.exception.utils.TechExceptionCodes;
import com.pareto.activities.exception.utils.ValidationError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
//@Profile("!debug")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements IErrorResponseBuilder {

    private static final String INVALID_REQUEST_FORMAT = "Invalid request data. Please check your request body.";

    @Override
    public ResponseEntity<Object> ErrorResponseBuilder(
            WebRequest request,
            HttpStatus httpStatus,
            Exception ex,
            Enum<? extends ExceptionKeyAndMessage> globalExceptionCodes
    ) {
        if (ex instanceof HttpMessageNotReadableException) {
            return ErrorResponseBuilder(
                    request,
                    httpStatus,
                    TechExceptionCodes.INVALID_REQUEST_EXCEPTION.getExceptionMessage(),
                    ((ExceptionKeyAndMessage) globalExceptionCodes).getExceptionCode(),
                    INVALID_REQUEST_FORMAT,
                    Collections.emptyList()
            );
        }
        else {
            return ErrorResponseBuilder(
                    request,
                    httpStatus,
                    ((ExceptionKeyAndMessage) globalExceptionCodes).getExceptionMessage() + "(" + ex.getMessage() + ")",
                    ((ExceptionKeyAndMessage) globalExceptionCodes).getExceptionCode(),
                    Collections.emptyList()
            );
        }
    }

    @Override
    public ResponseEntity<Object> ErrorResponseBuilder(
            WebRequest request,
            HttpStatus httpStatus,
            String techMessage,
            int code,
            List<?> errors
    ) {

        String exceptionMessage = GeneralExceptionMessages.getExceptionByCode(Constants.AZ);

        return ErrorResponseBuilder(
                request,
                httpStatus,
                techMessage,
                code,
                exceptionMessage,
                errors
        );
    }

    @Override
    public ResponseEntity<Object> ErrorResponseBuilder(
            WebRequest request,
            HttpStatus httpStatus,
            String techMessage,
            int code,
            String businessMessage,
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
                httpStatus.value()
        );
        attributes.put(
                HttpResponseConstants.ERROR,
                httpStatus.getReasonPhrase()
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
                httpStatus
        );
    }

    @Override
    public ResponseEntity<Object> ErrorResponseBuilder(
            WebRequest request,
            HttpStatus httpStatus,
            String techMessage,
            int code,
            String businessMessage,
            Map<String, ?> validationErrors
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
                httpStatus.value()
        );
        attributes.put(
                HttpResponseConstants.ERROR,
                httpStatus.getReasonPhrase()
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
                httpStatus
        );
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handle(
            GeneralException ex,
            WebRequest request
    ) {
        return ErrorResponseBuilder(
                request,
                ex.getHttpStatus(),
                ex.getMessage(),
                ex.getCode(),
                ex.getMessage(),
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

        throw ex;
//        return ofType(
//                request,
//                HttpStatus.BAD_REQUEST,
//                TechExceptionCodes.RUNTIME_EXCEPTION.getExceptionMessage(),
//                TechExceptionCodes.RUNTIME_EXCEPTION.getExceptionCode(),
//                Collections.emptyList()
//        );
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.ACCESS_DENIED
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.CONNECT_EXCEPTION
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
            return ErrorResponseBuilder(
                    request,
                    HttpStatus.NOT_FOUND,
                    "(" + TechExceptionCodes.FEIGN_CLIENT + ")" + "Card not found. Please check input details",
                    TechExceptionCodes.FEIGN_CLIENT.getExceptionCode(),
                    "Card not found. Please check input details",
                    Collections.emptyList()
            );
        }
        else {
            return ErrorResponseBuilder(
                    request,
                    HttpStatus.valueOf(ex.getStatus()),
                    "(" + TechExceptionCodes.FEIGN_CLIENT.getExceptionMessage() + ")" + ex.getMessage(),
                    TechExceptionCodes.FEIGN_CLIENT.getExceptionCode(),
                    ex.getMessage(),
                    Collections.emptyList()
            );
        }

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handle(
            MethodArgumentTypeMismatchException ex,
            WebRequest request
    ) {
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.METHOD_ARGUMENT_TYPE_MISMATCH
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.NO_SUCH_ELEMENT
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.KEY_STORE_EXCEPTION
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.SOCKET_EXCEPTION
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.DATE_TIME_PARSE
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.INVALID_FORMAT
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.NO_SUCH_BEAN_DEFINITION
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.NULL_POINTER
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.ILLEGAL_ARGUMENT
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.CONVERSION_EXCEPTION
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.UNEXPECTED_TYPE
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.ARITHMETIC
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
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                ex,
                TechExceptionCodes.CLASS_CAST_EXCEPTION
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
                                       .toString()
                        )
                        .rejectedValue(Objects
                                               .requireNonNull(p.getInvalidValue())
                                               .toString()
                        )
                        .rejectedMessage(p.getMessage())
                        .build())
                .collect(Collectors.toList())
                ;
        log.error(
                "javax.validation.ConstraintViolationException: {}",
                ex.getMessage()
        );
        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                TechExceptionCodes.CONSTRAINT_VIOLATION.getExceptionMessage(),
                TechExceptionCodes.CONSTRAINT_VIOLATION.getExceptionCode(),
                validationErrors
        );
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        return ErrorResponseBuilder(
                request,
                HttpStatus.BAD_REQUEST,
                TechExceptionCodes.METHOD_ARGUMENT_NOT_VALID.getExceptionMessage(),
                TechExceptionCodes.METHOD_ARGUMENT_NOT_VALID.getExceptionCode(),
                TechExceptionCodes.METHOD_ARGUMENT_NOT_VALID.getExceptionMessage(),

                ex
                        .getFieldErrors()
                        .stream()
                        .map(
                                fieldError -> new FieldError(
                                        fieldError.getField(),
                                        fieldError.getDefaultMessage(),
                                        fieldError.getRejectedValue()
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

    @Data
    @AllArgsConstructor
    static
    class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;
    }
}

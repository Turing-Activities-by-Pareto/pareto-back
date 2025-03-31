package com.pareto.activities.exception;

import com.pareto.activities.enums.BusinessStatus;
import org.springframework.http.HttpStatus;

public class BusinessException extends GeneralException {

    public BusinessException(BusinessStatus businessExceptionCodes) {
        super(businessExceptionCodes, HttpStatus.NOT_FOUND);
    }

    public BusinessException(
            BusinessStatus businessExceptionCodes,
            HttpStatus httpStatus
    ) {
        super(businessExceptionCodes, httpStatus);
    }

    public BusinessException(
            String message,
            BusinessStatus businessExceptionCodes,
            HttpStatus httpStatus
    ) {
        super(message, businessExceptionCodes, httpStatus);
    }

    public BusinessException(
            Throwable cause,
            BusinessStatus businessExceptionCodes,
            HttpStatus httpStatus
    ) {
        super(cause, businessExceptionCodes, httpStatus);
    }
}

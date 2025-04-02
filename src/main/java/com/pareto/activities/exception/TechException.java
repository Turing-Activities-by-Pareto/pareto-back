package com.pareto.activities.exception;

import com.pareto.activities.exception.utils.TechExceptionCodes;
import org.springframework.http.HttpStatus;

public class TechException extends GeneralException {

    public TechException(TechExceptionCodes techExceptionCodes) {
        super(
                techExceptionCodes,
                HttpStatus.NOT_FOUND
        );
    }

    public TechException(
            TechExceptionCodes techExceptionCodes,
            HttpStatus httpStatus
    ) {
        super(
                techExceptionCodes,
                httpStatus
        );
    }

    public TechException(
            String message,
            TechExceptionCodes techExceptionCodes,
            HttpStatus httpStatus
    ) {
        super(
                message,
                techExceptionCodes,
                httpStatus
        );
    }

    public TechException(
            Throwable cause,
            TechExceptionCodes techExceptionCodes,
            HttpStatus httpStatus
    ) {
        super(
                cause,
                techExceptionCodes,
                httpStatus
        );
    }
}


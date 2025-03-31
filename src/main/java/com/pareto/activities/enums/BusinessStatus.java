package com.pareto.activities.enums;


import com.pareto.activities.exception.utils.ExceptionKeyAndMessage;
import com.pareto.activities.exception.utils.HttpResponseConstants;

public enum BusinessStatus implements ExceptionKeyAndMessage {
    COMMON_SUCCESS(0, HttpResponseConstants.STATUS, "Successfully process!"),
    INVALID_ARGUMENTS(1001, HttpResponseConstants.VALIDATION_ERRORS, "Invalid parameters"),
    UNKNOWN_ERROR(9999, HttpResponseConstants.ERROR, "Internal Server Error"),
    DATA_NOT_FOUND(1002, HttpResponseConstants.ERROR, "Data not found"),
    INVALID_CARD(1003, HttpResponseConstants.VALIDATION_ERRORS, "Invalid card details"),
    INVALID_CIF_NUMBER(1004, HttpResponseConstants.VALIDATION_ERRORS, "Invalid cif code"),
    INVALID_VOEN(1005, HttpResponseConstants.VALIDATION_ERRORS, "Invalid voen"),
    INVALID_ACCOUNT(1006, HttpResponseConstants.VALIDATION_ERRORS, "Invalid account"),
    INVALID_AGREEMENT_NUMBER(1007, HttpResponseConstants.VALIDATION_ERRORS, "Invalid agreement number"),
    INVALID_AGREEMENT_ID(1008, HttpResponseConstants.VALIDATION_ERRORS, "Invalid agreement id"),
    INVALID_AGREEMENT_NAME(1009, HttpResponseConstants.VALIDATION_ERRORS, "Invalid agreement name"),
    INVALID_BRANCH(1010, HttpResponseConstants.VALIDATION_ERRORS, "Invalid branch"),
    INVALID_PSEUDO_PAN(1011, HttpResponseConstants.VALIDATION_ERRORS, "Invalid pseudo pan"),
    INVALID_BIRTH_DATE(1012, HttpResponseConstants.VALIDATION_ERRORS, "Birth Year can't be bigger than current year"),
    INVALID_AGE(1013, HttpResponseConstants.VALIDATION_ERRORS, "Credit is not available for ages under 18"),
    EMPTY_BIRTH_DATE(1014, HttpResponseConstants.VALIDATION_ERRORS, "BirthDate mustn't be null"),
    TRANSACTION_IS_NOT_FOUND(1015, HttpResponseConstants.VALIDATION_ERRORS, "This transaction is not found"),
    INVALID_TRANSACTION_DETAILS(1016, HttpResponseConstants.VALIDATION_ERRORS, "Invalid transaction details"),
    SESSION_IS_EXISTS(1017, HttpResponseConstants.VALIDATION_ERRORS, "This sessionId is exists"),
    EVENT_NOT_FOUND(1018, HttpResponseConstants.ERROR, "Event not found"),
    MINIO_ERROR(1024, HttpResponseConstants.ERROR, "Minio error");


    private final int code;
    private final String title;
    private final String message;

    BusinessStatus(
            int code,
            String title,
            String message
    ) {
        this.code = code;
        this.title = title;
        this.message = message;
    }

    @Override
    public int getExceptionCode() {
        return code;
    }

    @Override
    public String getExceptionTitle() {
        return title;
    }

    @Override
    public String getExceptionMessage() {
        return message;
    }
}

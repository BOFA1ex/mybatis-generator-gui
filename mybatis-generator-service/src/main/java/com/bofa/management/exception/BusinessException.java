package com.bofa.management.exception;

import org.h2.api.ErrorCode;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
public class BusinessException extends RuntimeException {

    public BusinessErrorCode errorCode;

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(BusinessErrorCode errorCode, String... message) {
        super(errorCode.getMessageAndCompletion(message));
        this.errorCode = errorCode;
    }

    public BusinessException(String message) {
        super(message);
    }

    public static void throwBusinessException(BusinessErrorCode errorCode, String... message) {
        throw new BusinessException(errorCode, message);
    }

    public static BusinessException mapperBusinessException(String message) {
        return new BusinessException(message);
    }

    public static void throwBusinessException(String message) {
        throw new BusinessException(message);
    }

    public static void throwBusinessException(String message, Throwable cause) {
        throw new BusinessException(message, cause);
    }

    public BusinessErrorCode getErrorCode() {
        return this.errorCode;
    }
}

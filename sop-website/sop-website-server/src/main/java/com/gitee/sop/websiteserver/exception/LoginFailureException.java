package com.gitee.sop.websiteserver.exception;

/**
 * @author tanghc
 */
public class LoginFailureException extends RuntimeException implements ExceptionCode {
    @Override
    public ErrorCode getCode() {
        return ErrorCode.LOGIN_FAIL;
    }
}

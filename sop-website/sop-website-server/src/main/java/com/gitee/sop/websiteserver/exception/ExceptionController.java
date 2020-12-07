package com.gitee.sop.websiteserver.exception;

import com.gitee.sop.websiteserver.bean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局错误处理
 *
 * @author tanghc
 */
@RestControllerAdvice
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(Exception e) {
        if (e instanceof ExceptionCode) {
            ExceptionCode exceptionCode = (ExceptionCode) e;
            ErrorCode errorCode = exceptionCode.getCode();
            logger.error("报错，code:{}, msg:{}", errorCode.getCode(), errorCode.getMsg(), e);
            return Result.err(errorCode.getCode(), errorCode.getMsg());
        }
        logger.error("未知错误：", e);
        return Result.err(e.getMessage());
    }

}

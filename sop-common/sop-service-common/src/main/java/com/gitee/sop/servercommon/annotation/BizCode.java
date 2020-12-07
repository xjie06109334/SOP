package com.gitee.sop.servercommon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 错误码
 *
 * @author tanghc
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizCode {

    /**
     * 错误码
     *
     * @return
     */
    String code();

    /**
     * 错误描述
     * @return
     */
    String msg();

    /**
     * 解决方案
     * @return
     */
    String solution() default "";
}

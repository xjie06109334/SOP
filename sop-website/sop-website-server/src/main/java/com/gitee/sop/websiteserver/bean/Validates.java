package com.gitee.sop.websiteserver.bean;

import com.gitee.sop.gatewaycommon.exception.ApiException;
import com.gitee.sop.gatewaycommon.message.ErrorMeta;
import org.apache.commons.lang.StringUtils;

import java.util.function.Supplier;

/**
 * @author tanghc
 */
public class Validates {

    public static void isTrue(boolean expression, Supplier<RuntimeException> supplier) {
        if (expression) {
            throw supplier.get();
        }
    }

    public static void isTrue(boolean expression, String msg) {
        isTrue(expression, () -> new RuntimeException(msg));
    }

}

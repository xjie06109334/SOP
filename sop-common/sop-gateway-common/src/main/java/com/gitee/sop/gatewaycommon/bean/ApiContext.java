package com.gitee.sop.gatewaycommon.bean;

/**
 * 应用上下文,方便获取信息
 *
 * @author tanghc
 */
public class ApiContext {


    public static ApiConfig getApiConfig() {
        return ApiConfig.getInstance();
    }

    public static void setApiConfig(ApiConfig apiConfig) {
        ApiConfig.setInstance(apiConfig);
    }

}

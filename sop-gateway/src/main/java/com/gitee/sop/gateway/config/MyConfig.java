package com.gitee.sop.gateway.config;

import com.gitee.sop.gatewaycommon.bean.ApiConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MyConfig {

    @PostConstruct
    public void after() {
        ApiConfig.getInstance().setTokenValidator(apiParam -> {
            // 获取客户端传递过来的token
            String token = apiParam.fetchAccessToken();
            return !StringUtils.isBlank(token);
            // TODO: 校验token有效性，可以从redis中读取

            // 返回true表示这个token真实、有效
        });
    }
}
package com.gitee.sop.bridge;

import com.gitee.sop.bridge.route.NacosRegistryListener;
import com.gitee.sop.gatewaycommon.route.RegistryListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tanghc
 */
@Configuration
public class SopRegisterAutoConfiguration {

    /**
     * 微服务路由加载
     */
    @Bean
    RegistryListener registryListenerNacos() {
        return new NacosRegistryListener();
    }
}

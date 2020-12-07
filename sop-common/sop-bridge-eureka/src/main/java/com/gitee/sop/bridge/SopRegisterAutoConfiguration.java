package com.gitee.sop.bridge;

import com.gitee.sop.bridge.route.EurekaRegistryListener;
import com.gitee.sop.gatewaycommon.route.RegistryListener;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.eureka.EurekaServerIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tanghc
 */
@Configuration
public class SopRegisterAutoConfiguration {

    /**
     * 负责获取eureka实例的metadata
     * @return
     */
    @Bean
    ServerIntrospector eurekaServerIntrospector() {
        return new EurekaServerIntrospector();
    }

    @Bean
    RegistryListener registryListenerEureka() {
        return new EurekaRegistryListener();
    }

}


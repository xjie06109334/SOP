package com.gitee.sop.servercommon.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;

import java.util.Map;

/**
 * @author tanghc
 */
@Slf4j
public class ServiceConfiguration extends SpringmvcConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty("spring.cloud.nacos.discovery.server-addr")
    public NacosWatch nacosWatch(NacosDiscoveryProperties nacosDiscoveryProperties, ObjectProvider<TaskScheduler> taskScheduler, Environment environment) {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        String contextPath = environment.getProperty(METADATA_SERVER_CONTEXT_PATH);
        // 将context-path信息加入到metadata中
        if (contextPath != null) {
            metadata.put(METADATA_SERVER_CONTEXT_PATH, contextPath);
        }
        // 在元数据中新增启动时间，不能修改这个值，不然网关拉取接口会有问题
        // 如果没有这个值，网关会忽略这个服务
        metadata.put("server.startup-time", String.valueOf(System.currentTimeMillis()));
        return new NacosWatch(nacosDiscoveryProperties, taskScheduler);
    }

}

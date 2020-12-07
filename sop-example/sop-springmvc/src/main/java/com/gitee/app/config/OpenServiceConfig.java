package com.gitee.app.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.NetUtils;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import com.gitee.sop.servercommon.configuration.SpringmvcConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * 使用支付宝开放平台功能
 *
 * @author tanghc
 */
@Slf4j
@EnableNacosDiscovery
public class OpenServiceConfig extends SpringmvcConfiguration {


    public static final String SPRING_APPLICATION_NAME = "spring.application.name";
    public static final String SERVER_CONTEXT_PATH = "server.servlet.context-path";
    public static final String SERVER_IP = "server.ip";
    public static final String SERVER_PORT = "server.port";
    public static final String METADATA_TIME_STARTUP = "server.startup-time";

    @Value("${spring.application.name}")
    private String serviceId;
    @Value("${server.port}")
    private int port;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @NacosInjected
    private NamingService namingService;

    @Override
    protected void doAfter() {
        super.doAfter();
        try {
            String ip = NetUtils.localIP();
            System.setProperty(SPRING_APPLICATION_NAME, serviceId);
            System.setProperty(SERVER_IP, ip);
            System.setProperty(SERVER_PORT, String.valueOf(port));
            System.setProperty(SERVER_CONTEXT_PATH, contextPath);

            Instance instance = this.getInstance(serviceId, ip, port, contextPath);
            namingService.registerInstance(serviceId, instance);
            log.info("注册到nacos, serviceId: {}, ip: {}, port: {}, contextPath: {}", serviceId, ip, port, contextPath);
        } catch (NacosException e) {
            log.error("注册nacos失败", e);
            throw new RuntimeException("注册nacos失败", e);
        }
    }

    private Instance getInstance(String serviceId, String ip, int port, String contextPath) {
        Instance instance = new Instance();
        instance.setServiceName(serviceId);
        instance.setIp(ip);
        instance.setPort(port);
        instance.getMetadata().put(METADATA_SERVER_CONTEXT_PATH, contextPath);
        instance.getMetadata().put(METADATA_TIME_STARTUP, String.valueOf(System.currentTimeMillis()));
        return instance;
    }

}

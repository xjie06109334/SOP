package com.gitee.sop.websiteserver.service.impl;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.gitee.sop.websiteserver.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author tanghc
 */
@Slf4j
public class NacosServerService implements ServerService  {

    @Value("${nacos.discovery.server-addr:${spring.cloud.nacos.discovery.server-addr:}}")
    private String nacosAddr;

    @Value("${nacos.discovery.namespace:${spring.cloud.nacos.discovery.namespace:}}")
    private String nacosNamespace;

    @Value("${nacos.discovery.group:${spring.cloud.nacos.discovery.group:DEFAULT_GROUP}}")
    private String nacosGroup;

    private NamingService namingService;

    @PostConstruct
    public void after() throws NacosException {
        if (StringUtils.isBlank(nacosAddr)) {
            throw new IllegalArgumentException("请在配置文件中指定nacos.discovery.server-addr参数");
        }
        Properties nacosProperties = new Properties();
        nacosProperties.put(PropertyKeyConst.SERVER_ADDR, nacosAddr);
        if (StringUtils.isNotBlank(nacosNamespace)) {
            nacosProperties.put(PropertyKeyConst.NAMESPACE, nacosNamespace);
        }
        namingService = NamingFactory.createNamingService(nacosProperties);
    }

    @Override
    public List<String> listServerHost(String serviceId) {
        Objects.requireNonNull(serviceId, "serviceId can not be null");
        List<String> list = Collections.emptyList();
        try {
            List<Instance> allInstances = namingService.getAllInstances(serviceId, nacosGroup);
            list = allInstances.stream().map(instance -> instance.getIp() + ":" + instance.getPort())
                    .collect(Collectors.toList());
        } catch (NacosException e) {
            log.error("获取实例host出错, serviceId:{}", serviceId, e);
        }
        return list;
    }
}

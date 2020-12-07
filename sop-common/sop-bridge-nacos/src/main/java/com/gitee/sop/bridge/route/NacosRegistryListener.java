package com.gitee.sop.bridge.route;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.gitee.sop.gatewaycommon.bean.InstanceDefinition;
import com.gitee.sop.gatewaycommon.bean.SopConstants;
import com.gitee.sop.gatewaycommon.route.BaseRegistryListener;
import com.gitee.sop.gatewaycommon.route.RegistryEvent;
import com.gitee.sop.gatewaycommon.route.ServiceHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 加载服务路由，nacos实现
 *
 * @author tanghc
 */
@Slf4j
public class NacosRegistryListener extends BaseRegistryListener {

    private volatile Set<NacosServiceHolder> cacheServices = new HashSet<>();

    @Value("${nacos.discovery.group:${spring.cloud.nacos.discovery.group:DEFAULT_GROUP}}")
    private String nacosGroup;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired(required = false)
    private List<RegistryEvent> registryEventList;

    @Override
    public synchronized void onEvent(ApplicationEvent applicationEvent) {
        List<NacosServiceHolder> serviceList = this.getServiceList();
        final Set<NacosServiceHolder> currentServices = new HashSet<>(serviceList);

        // 删除现有的，剩下的就是新服务
        currentServices.removeAll(cacheServices);
        // 如果有新的服务注册进来
        if (currentServices.size() > 0) {
            currentServices.forEach(nacosServiceHolder -> {
                Instance instance = nacosServiceHolder.getInstance();
                InstanceDefinition instanceDefinition = new InstanceDefinition();
                instanceDefinition.setInstanceId(instance.getInstanceId());
                instanceDefinition.setServiceId(nacosServiceHolder.getServiceId());
                instanceDefinition.setIp(instance.getIp());
                instanceDefinition.setPort(instance.getPort());
                instanceDefinition.setMetadata(instance.getMetadata());
                pullRoutes(instanceDefinition);
                if (registryEventList != null) {
                    registryEventList.forEach(registryEvent -> registryEvent.onRegistry(instanceDefinition));
                }
            });
        }

        // 如果有服务下线
        Set<String> removedServiceIdList = getRemovedServiceId(serviceList);
        // 移除
        this.doRemove(removedServiceIdList);

        cacheServices = new HashSet<>(serviceList);
    }

    /**
     * 获取建康的服务实例
     *
     * @return 没有返回空的list
     */
    private List<NacosServiceHolder> getServiceList() {
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        ListView<String> servicesOfServer = null;
        try {
            servicesOfServer = namingService.getServicesOfServer(1, Integer.MAX_VALUE);
        } catch (NacosException e) {
            log.error("namingService.getServicesOfServer()错误", e);
        }
        if (servicesOfServer == null || CollectionUtils.isEmpty(servicesOfServer.getData())) {
            return Collections.emptyList();
        }
        return servicesOfServer
                .getData()
                .stream()
                .map(serviceName -> {
                    List<Instance> allInstances;
                    try {
                        // 获取服务实例
                       allInstances = namingService.getAllInstances(serviceName, nacosGroup);
                    } catch (NacosException e) {
                        log.error("namingService.getAllInstances(serviceName)错误，serviceName：{}", serviceName, e);
                        return null;
                    }
                    if (CollectionUtils.isEmpty(allInstances)) {
                        return null;
                    }
                    return allInstances.stream()
                            // 只获取建康实例
                            .filter(Instance::isHealthy)
                            .map(instance -> {
                                String startupTime = instance.getMetadata().get(SopConstants.METADATA_KEY_TIME_STARTUP);
                                if (startupTime == null) {
                                    return null;
                                }
                                long time = NumberUtils.toLong(startupTime, 0);
                                return new NacosServiceHolder(serviceName, time, instance);
                            })
                            .filter(Objects::nonNull)
                            .max(Comparator.comparing(ServiceHolder::getLastUpdatedTimestamp))
                            .orElse(null);

                })
                .filter(Objects::nonNull)
                .filter(this::canOperator)
                .collect(Collectors.toList());
    }

    /**
     * 获取已经下线的serviceId
     *
     * @param serviceList 最新的serviceId集合
     * @return 返回已下线的serviceId
     */
    private Set<String> getRemovedServiceId(List<NacosServiceHolder> serviceList) {
        Set<String> cache = cacheServices.stream()
                .map(NacosServiceHolder::getServiceId)
                .collect(Collectors.toSet());

        Set<String> newList = serviceList.stream()
                .map(NacosServiceHolder::getServiceId)
                .collect(Collectors.toSet());

        cache.removeAll(newList);
        return cache;
    }

    private void doRemove(Set<String> deletedServices) {
        if (deletedServices == null) {
            return;
        }
        deletedServices.forEach(serviceId -> {
            this.removeRoutes(serviceId);
            if (registryEventList != null) {
                registryEventList.forEach(registryEvent -> registryEvent.onRemove(serviceId));
            }
        });
    }

}

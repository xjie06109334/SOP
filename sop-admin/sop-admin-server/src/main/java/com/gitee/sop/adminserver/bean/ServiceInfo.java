package com.gitee.sop.adminserver.bean;

import java.util.List;

/**
 * @author tanghc
 */
public class ServiceInfo {
    /** 服务名称 */
    private String serviceId;
    /** 实例列表 */
    private List<ServiceInstance> instances;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<ServiceInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<ServiceInstance> instances) {
        this.instances = instances;
    }
}

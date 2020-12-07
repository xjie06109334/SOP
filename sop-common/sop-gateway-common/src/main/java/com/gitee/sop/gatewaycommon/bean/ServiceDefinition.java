package com.gitee.sop.gatewaycommon.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author tanghc
 */
@Data
public class ServiceDefinition {

    /**
     * 服务名称，对应spring.application.name
     */
    private String serviceId;

    public ServiceDefinition(String serviceId) {
        this.serviceId = serviceId;
    }

    public String fetchServiceIdLowerCase() {
        return serviceId.toLowerCase();
    }
}
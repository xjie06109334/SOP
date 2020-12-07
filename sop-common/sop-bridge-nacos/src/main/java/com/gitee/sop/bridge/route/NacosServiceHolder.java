package com.gitee.sop.bridge.route;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.gitee.sop.gatewaycommon.route.ServiceHolder;

/**
 * @author tanghc
 */
public class NacosServiceHolder extends ServiceHolder {

    private final Instance instance;

    public NacosServiceHolder(String serviceId, long lastUpdatedTimestamp, Instance instance) {
        super(serviceId, lastUpdatedTimestamp);
        this.instance = instance;
    }

    public Instance getInstance() {
        return instance;
    }
}

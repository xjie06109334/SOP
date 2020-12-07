package com.gitee.sop.gatewaycommon.gateway.route;

import com.gitee.sop.gatewaycommon.bean.RouteDefinition;
import com.gitee.sop.gatewaycommon.bean.ServiceDefinition;
import com.gitee.sop.gatewaycommon.bean.TargetRoute;

/**
 * @author tanghc
 */
public class GatewayTargetRoute implements TargetRoute {

    private ServiceDefinition serviceDefinition;
    private RouteDefinition routeDefinition;


    public GatewayTargetRoute(ServiceDefinition serviceDefinition, RouteDefinition routeDefinition) {
        this.serviceDefinition = serviceDefinition;
        this.routeDefinition = routeDefinition;
    }

    @Override
    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    @Override
    public RouteDefinition getRouteDefinition() {
        return routeDefinition;
    }
}

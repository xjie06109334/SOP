package com.gitee.sop.gatewaycommon.bean;

/**
 * @author tanghc
 */
public interface TargetRoute {

    /**
     * 返回服务信息
     *
     * @return 返回服务信息
     */
    ServiceDefinition getServiceDefinition();

    /**
     * 返回微服务路由对象
     *
     * @return 返回微服务路由对象
     */
    RouteDefinition getRouteDefinition();

}

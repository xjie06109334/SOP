package com.gitee.sop.gateway.interceptor;

import com.gitee.sop.gatewaycommon.interceptor.RouteInterceptor;
import com.gitee.sop.gatewaycommon.interceptor.RouteInterceptorContext;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import com.gitee.sop.gatewaycommon.sync.SopAsyncConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用于收集监控数据
 *
 * @author tanghc
 */
@Component
@Slf4j
public class MonitorRouteInterceptor implements RouteInterceptor {

    @Autowired
    SopAsyncConfigurer sopAsyncConfigurer;

    @Autowired
    MonitorRouteInterceptorService monitorRouteInterceptorService;

    @Override
    public void preRoute(RouteInterceptorContext context) {
    }

    @Override
    public void afterRoute(RouteInterceptorContext context) {
        sopAsyncConfigurer.getAsyncExecutor().execute(()-> {
            monitorRouteInterceptorService.storeRequestInfo(context);
        });
    }

    @Override
    public int getOrder() {
        return -1000;
    }


}

package com.gitee.sop.gateway.interceptor;

import com.gitee.sop.gatewaycommon.interceptor.RouteInterceptor;
import com.gitee.sop.gatewaycommon.interceptor.RouteInterceptorContext;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 演示拦截器
 *
 * @author tanghc
 */
@Slf4j
@Component
public class MyRouteInterceptor implements RouteInterceptor {

    @Override
    public void preRoute(RouteInterceptorContext context) {
        ApiParam apiParam = context.getApiParam();
        log.info("请求接口:{}, request_id:{}, app_id:{}, ip:{}",
                apiParam.fetchNameVersion(),
                apiParam.fetchRequestId(),
                apiParam.fetchAppKey(),
                apiParam.fetchIp()
        );
    }

    @Override
    public void afterRoute(RouteInterceptorContext context) {
    }

    @Override
    public int getOrder() {
        return 0;
    }

}

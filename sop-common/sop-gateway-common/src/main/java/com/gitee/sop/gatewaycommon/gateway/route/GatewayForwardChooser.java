package com.gitee.sop.gatewaycommon.gateway.route;

import com.gitee.sop.gatewaycommon.bean.SopConstants;
import com.gitee.sop.gatewaycommon.bean.TargetRoute;
import com.gitee.sop.gatewaycommon.gateway.ServerWebExchangeUtil;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import com.gitee.sop.gatewaycommon.route.BaseForwardChooser;
import com.gitee.sop.gatewaycommon.route.ForwardInfo;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author tanghc
 */
public class GatewayForwardChooser extends BaseForwardChooser<ServerWebExchange> {

    @Override
    public ApiParam getApiParam(ServerWebExchange exchange) {
        return ServerWebExchangeUtil.getApiParam(exchange);
    }

    @Override
    public ForwardInfo getForwardInfo(ServerWebExchange exchange) {
        // 如果有异常，直接跳转到异常处理
        if (ServerWebExchangeUtil.getThrowable(exchange) != null) {
            return ForwardInfo.getErrorForwardInfo();
        }
        ForwardInfo forwardInfo = super.getForwardInfo(exchange);
        TargetRoute targetRoute = forwardInfo.getTargetRoute();
        exchange.getAttributes().put(SopConstants.CACHE_ROUTE_INFO, targetRoute.getRouteDefinition());
        return forwardInfo;
    }
}

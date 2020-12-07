package com.gitee.sop.gatewaycommon.monitor;

import lombok.Data;

/**
 * @author tanghc
 */
@Data
public class RouteErrorCount {

    private String routeId;
    private Integer count;
}

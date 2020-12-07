package com.gitee.sop.adminserver.api.service.param;

import com.gitee.fastmybatis.core.query.annotation.Condition;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tanghc
 */
@Getter
@Setter
public class InstanceMonitorSearchParam {
    @Condition(column = "t.route_id")
    private String routeId;
}

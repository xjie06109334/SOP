package com.gitee.sop.adminserver.api.service.param;

import com.gitee.easyopen.doc.annotation.ApiDocField;
import com.gitee.fastmybatis.core.query.Operator;
import com.gitee.fastmybatis.core.query.annotation.Condition;
import com.gitee.fastmybatis.core.query.param.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tanghc
 */
@Getter
@Setter
public class MonitorSearchParam extends PageParam {
    @ApiDocField(description = "服务名serviceId")
    @Condition(column = "service_id", operator = Operator.like, ignoreEmptyString = true)
    private String serviceId;

    @ApiDocField(description = "路由id")
    @Condition(column = "route_id", operator = Operator.like, ignoreEmptyString = true)
    private String routeId;
}

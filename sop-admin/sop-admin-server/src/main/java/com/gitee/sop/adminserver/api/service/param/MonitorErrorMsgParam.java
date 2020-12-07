package com.gitee.sop.adminserver.api.service.param;

import com.gitee.fastmybatis.core.query.annotation.Condition;
import com.gitee.fastmybatis.core.query.param.PageParam;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author tanghc
 */
@Getter
@Setter
public class MonitorErrorMsgParam extends PageParam {
    @NotBlank(message = "routeId不能为空")
    private String routeId;

    @Condition(ignoreEmptyString = true)
    private String instanceId;
}

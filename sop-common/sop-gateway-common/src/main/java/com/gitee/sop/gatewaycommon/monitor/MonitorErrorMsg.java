package com.gitee.sop.gatewaycommon.monitor;

import lombok.Data;

/**
 * @author thc
 */
@Data
public class MonitorErrorMsg {
    /** 错误id,md5(error_msg), 数据库字段：error_id */
    private String errorId;

    /** 实例id, 数据库字段：instance_id */
    private String instanceId;

    /**  数据库字段：route_id */
    private String routeId;

    /**  数据库字段：isp_id */
    private Long ispId;

    /**  数据库字段：error_msg */
    private String errorMsg;

    /** http status，非200错误 */
    private Integer errorStatus;

    /** 错误次数, 数据库字段：count */
    private Integer count;
}
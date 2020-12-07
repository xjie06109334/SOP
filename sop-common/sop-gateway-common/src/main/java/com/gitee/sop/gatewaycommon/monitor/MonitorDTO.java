package com.gitee.sop.gatewaycommon.monitor;

import lombok.Data;

import java.util.Collection;

/**
 * 每个接口 总调用流量，最大时间，最小时间，总时长，平均时长，调用次数，成功次数，失败次数，错误查看。
 *
 * @author tanghc
 */
@Data
public class MonitorDTO {

    /**
     * 路由id
     */
    private String routeId;

    /**
     * 接口名
     */
    private String name;
    /**
     * 版本号
     */
    private String version;
    /**
     * serviceId
     */
    private String serviceId;

    /**
     * 实例id
     */
    private String instanceId;

    /** 请求耗时最长时间, 数据库字段：max_time */
    private Integer maxTime;

    /** 请求耗时最小时间, 数据库字段：min_time */
    private Integer minTime;

    /**
     * 总时长
     */
    private Long totalTime;

    /** 总调用次数, 数据库字段：total_request_count */
    private Long totalRequestCount;

    /** 成功次数, 数据库字段：success_count */
    private Long successCount;

    /** 失败次数（业务主动抛出的异常算作成功，如参数校验，未知的错误算失败）, 数据库字段：error_count */
    private Long errorCount;

    /**
     * 错误信息
     */
    private Collection<MonitorErrorMsg> errorMsgList;

}

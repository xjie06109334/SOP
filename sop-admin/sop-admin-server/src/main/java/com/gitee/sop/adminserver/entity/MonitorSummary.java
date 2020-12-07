package com.gitee.sop.adminserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author tanghc
 */
@Data
public class MonitorSummary {

    private static final AtomicInteger i = new AtomicInteger();

    private Integer id = i.incrementAndGet();

    private String routeId;

    private String name;
    private String version;

    /**  数据库字段：service_id */
    private String serviceId;

    /**  数据库字段：instance_id */
    private String instanceId;

    /** 请求耗时最长时间, 数据库字段：max_time */
    private Integer maxTime;

    /** 请求耗时最小时间, 数据库字段：min_time */
    private Integer minTime;

    /** 总时长，毫秒, 数据库字段：total_time */
    private Long totalTime;

    /** 总调用次数, 数据库字段：total_request_count */
    private Long totalRequestCount;

    /** 成功次数, 数据库字段：success_count */
    private Long successCount;

    /** 失败次数（业务主动抛出的异常算作成功，如参数校验，未知的错误算失败）, 数据库字段：error_count */
    private Long errorCount;

    /** 未解决的错误数量 */
    private Long unsolvedErrorCount;

    private Float avgTime;

    private Boolean hasChildren;
}

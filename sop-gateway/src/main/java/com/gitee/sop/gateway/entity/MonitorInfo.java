package com.gitee.sop.gateway.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * 表名：monitor_info
 * 备注：接口监控信息
 *
 * @author tanghc
 */
@Table(name = "monitor_info")
@Data
public class MonitorInfo {
    /**  数据库字段：id */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 路由id, 数据库字段：route_id */
    private String routeId;

    /** 接口名, 数据库字段：name */
    private String name;

    /** 版本号, 数据库字段：version */
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

    /**  数据库字段：gmt_create */
    private Date gmtCreate;

    /**  数据库字段：gmt_modified */
    private Date gmtModified;
}

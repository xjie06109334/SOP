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
 * 表名：monitor_info_error
 *
 * @author tanghc
 */
@Table(name = "monitor_info_error")
@Data
public class MonitorInfoError {
    /**  数据库字段：id */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 错误id,md5Hex(instanceId + routeId + errorMsg), 数据库字段：error_id */
    private String errorId;

    /** 实例id, 数据库字段：instance_id */
    private String instanceId;

    /**  数据库字段：route_id */
    private String routeId;

    /**  数据库字段：error_msg */
    private String errorMsg;

    /** http status，非200错误, 数据库字段：error_status */
    private Integer errorStatus;

    /** 错误次数, 数据库字段：count */
    private Integer count;

    /**  数据库字段：is_deleted */
    @com.gitee.fastmybatis.core.annotation.LogicDelete
    private Byte isDeleted;

    /**  数据库字段：gmt_create */
    private Date gmtCreate;

    /**  数据库字段：gmt_modified */
    private Date gmtModified;
}

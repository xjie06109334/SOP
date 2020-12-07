package com.gitee.sop.gateway.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * 表名：isv_info
 * 备注：isv信息表
 *
 * @author tanghc
 */
@Table(name = "isv_info")
@Data
public class IsvInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**  数据库字段：id */
    private Long id;

    /** appKey, 数据库字段：app_key */
    private String appKey;

    /** 1启用，2禁用, 数据库字段：status */
    private Byte status;

    /** 1:RSA2,2:MD5, 数据库字段：sign_type */
    private Byte signType;

    /**  数据库字段：gmt_create */
    private Date gmtCreate;

    /**  数据库字段：gmt_modified */
    private Date gmtModified;
}

package com.gitee.sop.websiteserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * 表名：isp_resource
 * 备注：ISP资源表
 *
 * @author tanghc
 */
@Table(name = "isp_resource")
@Data
public class IspResource {
    /**  数据库字段：id */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 资源名称, 数据库字段：name */
    private String name;

    private String version;

    /** 资源内容（URL）, 数据库字段：content */
    private String content;

    private String extContent;

    /** 资源类型：0：SDK链接, 数据库字段：type */
    private Byte type;

    /**  数据库字段：is_deleted */
    @com.gitee.fastmybatis.core.annotation.LogicDelete
    private Byte isDeleted;

    /**  数据库字段：gmt_create */
    private Date gmtCreate;

    /**  数据库字段：gmt_modified */
    private Date gmtModified;
}

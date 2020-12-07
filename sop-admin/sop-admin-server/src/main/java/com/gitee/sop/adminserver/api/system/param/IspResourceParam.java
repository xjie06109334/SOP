package com.gitee.sop.adminserver.api.system.param;

import lombok.Data;


/**
 * 表名：isp_resource
 * 备注：ISP资源表
 *
 * @author tanghc
 */
@Data
public class IspResourceParam {

    private Long id;

    /** 资源名称, 数据库字段：name */
    private String name;

    private String version;

    /** 资源内容（URL）, 数据库字段：content */
    private String content;

    private String extContent;

}

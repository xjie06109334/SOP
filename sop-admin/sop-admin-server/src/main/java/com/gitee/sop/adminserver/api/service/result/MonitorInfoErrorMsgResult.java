package com.gitee.sop.adminserver.api.service.result;

import lombok.Data;

import java.util.Date;

/**
 * @author tanghc
 */
@Data
public class MonitorInfoErrorMsgResult {

    private String routeId;

    private String errorId;

    /** 错误信息, 数据库字段：error_msg */
    private String errorMsg;

    private Integer errorStatus;

    private Integer count;

    /**  数据库字段：gmt_modified */
    private Date gmtModified;
}

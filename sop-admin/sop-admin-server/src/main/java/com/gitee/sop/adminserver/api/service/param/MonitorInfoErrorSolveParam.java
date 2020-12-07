package com.gitee.sop.adminserver.api.service.param;

import com.gitee.fastmybatis.core.query.annotation.Condition;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tanghc
 */
@Data
public class MonitorInfoErrorSolveParam {
    /** 错误id,md5(error_msg), 数据库字段：error_id */
    @NotBlank
    @Condition(index = 1)
    private String errorId;


}

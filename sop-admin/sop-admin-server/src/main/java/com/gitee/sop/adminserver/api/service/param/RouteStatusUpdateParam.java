package com.gitee.sop.adminserver.api.service.param;

import com.gitee.easyopen.doc.annotation.ApiDocField;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tanghc
 */
@Getter
@Setter
public class RouteStatusUpdateParam {

    /**
     * 路由的Id
     */
    @NotBlank(message = "id不能为空")
    @ApiDocField(description = "路由id")
    private String id;

    /**
     * 状态
     */
    @NotNull
    @ApiDocField(description = "状态，0：审核，1：启用，2：禁用")
    private Byte status;

}

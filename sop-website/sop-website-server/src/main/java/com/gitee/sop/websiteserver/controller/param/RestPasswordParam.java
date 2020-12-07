package com.gitee.sop.websiteserver.controller.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tanghc
 */
@Data
public class RestPasswordParam {

    @NotBlank
    private String email;

    @NotNull
    private Long time;

    @NotBlank
    private String sign;

    @NotBlank(message = "密码不能为空")
    private String password;

}

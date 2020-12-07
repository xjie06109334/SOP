package com.gitee.sop.websiteserver.controller.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tanghc
 */
@Data
public class UpdatePasswordParam {

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @NotBlank(message = "密码不能为空")
    private String password;

}

package com.gitee.sop.websiteserver.controller.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tanghc
 */
@Data
public class RegParam {

    @NotBlank(message = "username can not be null")
    @Length(max = 128)
    private String username;

    @NotBlank(message = "password can not be null")
    @Length(max = 64)
    private String password;

    @Length(max = 64)
    private String namespace;

    @NotNull(message = "type can not be null")
    private Byte type;

    @Length(max = 64)
    private String company;


}

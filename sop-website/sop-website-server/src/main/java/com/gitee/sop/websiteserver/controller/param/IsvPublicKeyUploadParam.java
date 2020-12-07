package com.gitee.sop.websiteserver.controller.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tanghc
 */
@Data
public class IsvPublicKeyUploadParam {

    @NotBlank
    private String publicKeyIsv;
}

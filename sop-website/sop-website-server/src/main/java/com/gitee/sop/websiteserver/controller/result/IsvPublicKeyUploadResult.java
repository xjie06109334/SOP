package com.gitee.sop.websiteserver.controller.result;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tanghc
 */
@Data
public class IsvPublicKeyUploadResult {

    private String publicKeyIsv;

    private String publicKeyPlatform;

    private Integer isUploadPublicKey;

}

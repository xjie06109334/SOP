package com.gitee.sop.websiteserver.entity;

import lombok.Data;

/**
 * @author tanghc
 */
@Data
public class IsvPortal {

    private Long userId;

    /** isv appId */
    private String appId;

    /** isv上传的公钥, 用户验证客户端签名 */
    private transient String publicKeyIsv;

}

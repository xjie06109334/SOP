package com.gitee.sop.websiteserver.bean;

import lombok.Data;

/**
 * @author tanghc
 */
@Data
public class LoginUser {
    private Long userId;
    /** appKey, 数据库字段：app_key */
    private String appKey;

    /** 开发者生成的私钥（交给开发者）, 数据库字段：private_key_isv */
    private String privateKeyIsv;
}

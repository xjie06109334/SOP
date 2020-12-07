package com.gitee.sop.websiteserver.controller.result;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @author tanghc
 */
@Getter
@Setter
public class DocVO {
    private String gatewayUrl;
    private String appId;
    private String urlTest;
    private String urlProd;
    private Collection<MenuProject> menuProjects;
}

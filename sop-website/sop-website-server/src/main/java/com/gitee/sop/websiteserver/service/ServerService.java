package com.gitee.sop.websiteserver.service;

import java.util.List;

/**
 * @author tanghc
 */
public interface ServerService {
    /**
     * 获取服务host，ip:port
     * @param serviceId serviceId
     * @return 返回host列表
     */
    List<String> listServerHost(String serviceId);
}

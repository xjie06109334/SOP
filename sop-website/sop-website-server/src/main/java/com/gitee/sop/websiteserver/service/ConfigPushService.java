package com.gitee.sop.websiteserver.service;

import com.alibaba.fastjson.JSON;
import com.gitee.sop.websiteserver.bean.ChannelMsg;
import com.gitee.sop.websiteserver.bean.GatewayPushDTO;
import com.gitee.sop.websiteserver.bean.HttpTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanghc
 */
@Slf4j
@Service
public class ConfigPushService {

    private static final String GATEWAY_PUSH_URL = "http://%s/sop/configChannelMsg";
    private static final String API_GATEWAY_SERVICE_ID = "sop-gateway";

    private static HttpTool httpTool = new HttpTool();

    @Value("${sop.secret:MZZOUSTua6LzApIWXCwEgbBmxSzpzC}")
    private String secret;

    @Autowired
    private ServerService serverService;

    public void publishConfig(String dataId, String groupId, ChannelMsg channelMsg) {
        GatewayPushDTO gatewayPushDTO = new GatewayPushDTO(dataId, groupId, channelMsg);
        // 获取网关host，ip:port
        Collection<String> hostList = serverService.listServerHost(API_GATEWAY_SERVICE_ID);
        this.pushByHost(hostList, gatewayPushDTO);
    }

    private void pushByHost(Collection<String> hosts, GatewayPushDTO gatewayPushDTO) {
        for (String host : hosts) {
            String url = String.format(GATEWAY_PUSH_URL, host);
            log.info("推送配置, dataId={}, groupId={}, operation={}， url={}",
                    gatewayPushDTO.getDataId()
                    , gatewayPushDTO.getGroupId()
                    , gatewayPushDTO.getChannelMsg().getOperation()
                    , url);
            try {
                String requestBody = JSON.toJSONString(gatewayPushDTO);
                Map<String, String> header = new HashMap<>(8);
                header.put("sign", buildRequestBodySign(requestBody, secret));
                String resp = httpTool.requestJson(url, requestBody, header);
                if (!"ok".equals(resp)) {
                    throw new IOException(resp);
                }
            } catch (IOException e) {
                log.error("nacos配置失败, dataId={}, groupId={}, operation={}， url={}",
                        gatewayPushDTO.getDataId()
                        , gatewayPushDTO.getGroupId()
                        , gatewayPushDTO.getChannelMsg().getOperation()
                        , url
                        , e);
                throw new RuntimeException("推送配置失败");
            }
        }
    }

    public static String buildRequestBodySign(String requestBody, String secret) {
        String signContent = secret + requestBody + secret;
        return DigestUtils.md5Hex(signContent);
    }

}

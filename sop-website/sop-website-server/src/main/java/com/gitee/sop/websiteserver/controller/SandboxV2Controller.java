package com.gitee.sop.websiteserver.controller;

import com.alibaba.fastjson.JSON;
import com.gitee.sop.websiteserver.bean.HttpTool;
import com.gitee.sop.websiteserver.sign.AlipayApiException;
import com.gitee.sop.websiteserver.sign.AlipaySignature;
import com.gitee.sop.websiteserver.util.UploadUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 沙箱环境代理类
 *
 * @author tanghc
 */
@Slf4j
@RestController
@RequestMapping("sandbox")
public class SandboxV2Controller {

    @Value("${api.url-sandbox}")
    private String url;

    static HttpTool httpTool = new HttpTool();

    @RequestMapping("/test_v2")
    public void proxy(
            @RequestParam(required = false) String gatewayUrl
            , @RequestParam String appId
            , @RequestParam String privateKey
            , @RequestParam(required = false) String token
            , @RequestParam String method
            , @RequestParam String version
            , @RequestParam String bizContent
            , @RequestParam(defaultValue = "get") String httpMethod
            , @RequestParam(defaultValue = "false") boolean isDownloadRequest
            , HttpServletRequest request
            , HttpServletResponse response
    ) throws AlipayApiException, IOException {

        Assert.isTrue(StringUtils.isNotBlank(appId), "AppId不能为空");
        Assert.isTrue(StringUtils.isNotBlank(privateKey), "PrivateKey不能为空");
        Assert.isTrue(StringUtils.isNotBlank(method), "method不能为空");
        if (StringUtils.isEmpty(gatewayUrl)) {
            gatewayUrl = url;
        }
        // 公共请求参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("app_id", appId);
        params.put("method", method);
        params.put("format", "json");
        params.put("charset", "utf-8");
        params.put("sign_type", "RSA2");
        params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("version", version);

        if (StringUtils.isNotBlank(token)) {
            params.put("app_auth_token", token);
        }

        // 业务参数
        params.put("biz_content", bizContent);

        String paramsQuery = buildParamQuery(params);

        String content = AlipaySignature.getSignContent(params);

        String sign = null;
        try {
            sign = AlipaySignature.rsa256Sign(content, privateKey, "utf-8");
        } catch (AlipayApiException e) {
            log.error("构建签名失败", e);
            throw new RuntimeException("构建签名失败", e);
        }

        params.put("sign", sign);

        Collection<MultipartFile> uploadFiles = UploadUtil.getUploadFiles(request);
        List<HttpTool.UploadFile> files = uploadFiles.stream()
                .map(multipartFile -> {
                    try {
                        return new HttpTool.UploadFile(multipartFile.getName(), multipartFile.getOriginalFilename(), multipartFile.getBytes());
                    } catch (IOException e) {
                        log.error("封装文件失败", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        try {
            Response resp = httpTool.request(gatewayUrl, params, Collections.emptyMap(), HttpTool.HTTPMethod.fromValue(httpMethod), files);
            ResponseBody body = resp.body();
            if (body == null) {
                return;
            }
            Map<String, List<String>> headersMap = resp.headers().toMultimap();
            Map<String, String> targetHeaders = new HashMap<>(headersMap.size() * 2);
            headersMap.forEach((key, value) -> {
                String headerValue = String.join(",", value);
                response.setHeader(key, headerValue);
                targetHeaders.put(key, headerValue);
            });
            response.addHeader("target-response-headers", JSON.toJSONString(targetHeaders));
            response.addHeader("sendbox-params", UriUtils.encode(paramsQuery, StandardCharsets.UTF_8));
            response.addHeader("sendbox-beforesign", UriUtils.encode(content, StandardCharsets.UTF_8));
            response.addHeader("sendbox-sign", UriUtils.encode(sign, StandardCharsets.UTF_8));
            IOUtils.copy(body.byteStream(), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            log.error("请求失败", e);
            throw new RuntimeException("请求失败");
        }
    }

    @Data
    public static class SandboxResult {
        private String params;
        private String beforeSign;
        private String sign;

        private Object apiResult;
    }


    /**
     * 发送get请求
     *
     * @param url
     * @return JSON或者字符串
     * @throws Exception
     */
    public static String get(String url, Map<String, String> params) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            List<NameValuePair> nameValuePairs = params.entrySet()
                    .stream()
                    .map(entry -> new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())))
                    .collect(Collectors.toList());
            /**
             * 包装成一个Entity对象
             */
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            //参数转换为字符串
            String paramsStr = EntityUtils.toString(entity);
            url = url + "?" + paramsStr;
            /**
             * 创建一个post对象
             */
            HttpGet get = new HttpGet(url);

            /**
             * 执行post请求
             */
            response = httpClient.execute(get);
            /**
             * 通过EntityUitls获取返回内容
             */
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(httpClient);
            IOUtils.closeQuietly(response);
        }
        return null;
    }

    protected String buildParamQuery(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.substring(1);
    }
}

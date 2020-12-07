package com.gitee.sop.servercommon.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author tanghc
 */
@Slf4j
public class OpenUtil {

    public static final String MULTIPART = "multipart/";

    /**
     * 将get类型的参数转换成map，
     *
     * @param query charset=utf-8&biz_content=xxx
     * @return 返回map参数
     */
    public static Map<String, Object> parseQueryToMap(String query) {
        if (query == null) {
            return Collections.emptyMap();
        }
        String[] queryList = StringUtils.split(query, '&');
        Map<String, Object> params = new HashMap<>(16);
        for (String param : queryList) {
            String[] paramArr = param.split("\\=");
            if (paramArr.length == 2) {
                try {
                    params.put(paramArr[0], URLDecoder.decode(paramArr[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else if (paramArr.length == 1) {
                params.put(paramArr[0], "");
            }
        }
        return params;
    }

    /**
     * 获取request中的参数
     *
     * @param request request对象
     * @return 返回JSONObject
     */
    public static JSONObject getRequestParams(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            contentType = "";
        }
        contentType = contentType.toLowerCase();
        JSONObject jsonObject;
        if (StringUtils.containsAny(contentType, MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE)) {
            try {
                String requestJson = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
                if (StringUtils.isBlank(requestJson)) {
                    jsonObject = new JSONObject();
                } else {
                    jsonObject = JSON.parseObject(requestJson);
                }
            } catch (Exception e) {
                jsonObject = new JSONObject();
                log.error("获取文本数据流失败", e);
            }
        } else {
            Map<String, Object> params = convertRequestParamsToMap(request);
            jsonObject = new JSONObject(params);
        }
        return jsonObject;
    }


    /**
     * request中的参数转换成map
     *
     * @param request request对象
     * @return 返回参数键值对
     */
    public static Map<String, Object> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap == null || paramMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Object> retMap = new HashMap<String, Object>(paramMap.size());

        Set<Map.Entry<String, String[]>> entrySet = paramMap.entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            if (values.length >= 1) {
                retMap.put(name, values[0]);
            }
        }

        return retMap;
    }

    public static boolean validateSimpleSign(HttpServletRequest request, String secret) {
        String time = request.getParameter("time");
        String sign = request.getParameter("sign");
        if (StringUtils.isAnyBlank(time, sign)) {
            return false;
        }
        String source = secret + time + secret;
        String serverSign = DigestUtils.md5DigestAsHex(source.getBytes());
        return serverSign.equals(sign);
    }

    /**
     * 是否是文件上传请求
     *
     * @param request 请求
     * @return true：是
     */
    public static boolean isMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        // Don't use this filter on GET method
        if (contentType == null) {
            return false;
        }
        return contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART);
    }

}

package com.gitee.sop.websiteserver.util;

import com.gitee.sop.websiteserver.bean.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Base64Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author tanghc
 */
@Slf4j
public class GenerateUtil {

    private static long workerId = 0;
    private static SnowflakeIdWorker appIdWorker = new SnowflakeIdWorker(workerId++, 0);
    private static SnowflakeIdWorker secretWorker = new SnowflakeIdWorker(workerId++, 0);
    private static SnowflakeIdWorker fileWorker = new SnowflakeIdWorker(workerId++, 0);
    private static SnowflakeIdWorker orderNoWorker = new SnowflakeIdWorker(workerId++, 1);

    public static String generateServiceId() {
        return getUUID();
    }

    public static String generateIsvAppId() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + appIdWorker.nextId();
    }

    public static String generateNamespace() {
        return RandomUtil.createRandom();
    }

    public static String generateSecret() {
        byte[] bytes = DigestUtils.sha256(secretWorker.nextId() + getUUID());
        return Base64Utils.encodeToUrlSafeString(bytes).substring(1).replace("=", "");
    }

    public static String getUserPassword(String username, String password) {
        return DigestUtils.sha256Hex(username + password + username);
    }

    public static String generateVerifyFile() {
        return DigestUtils.md5Hex(String.valueOf(fileWorker.nextId()));
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateOrderNo() {
        return String.valueOf(orderNoWorker.nextId());
    }
}

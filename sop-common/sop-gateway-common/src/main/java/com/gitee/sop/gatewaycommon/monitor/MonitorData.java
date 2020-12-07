package com.gitee.sop.gatewaycommon.monitor;

import com.gitee.sop.gatewaycommon.bean.LRUCache;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 每个接口 总调用流量，最大时间，最小时间，总时长，平均时长，调用次数，成功次数，失败次数，错误查看。
 *
 * @author tanghc
 */
@Data
public class MonitorData {

    public static final int LIMIT_SIZE = 50;


    private String routeId;

    /**
     * 接口名
     */
    private String name;
    /**
     * 版本号
     */
    private String version;
    /**
     * serviceId
     */
    private String serviceId;

    private String instanceId;

    /**
     * 请求耗时最长时间
     */
    private Integer maxTime;
    /**
     * 请求耗时最小时间
     */
    private Integer minTime;
    /**
     * 总时长
     */
    private AtomicInteger totalTime;
    /**
     * 总调用次数
     */
    private AtomicInteger totalRequestCount;
    /**
     * 成功次数
     */
    private AtomicInteger successCount;
    /**
     * 失败次数（业务主动抛出的异常算作成功，如参数校验，未知的错误算失败）
     */
    private AtomicInteger errorCount;

    /**
     * 错误信息,key: errorId
     */
    private LRUCache<String, MonitorErrorMsg> monitorErrorMsgMap;

    /**
     * 下一次刷新到数据库的时间
     */
    private LocalDateTime flushTime;

    public synchronized void storeMaxTime(int spendTime) {
        if (spendTime > maxTime) {
            maxTime = spendTime;
        }
    }

    public synchronized void storeMinTime(int spendTime) {
        if (minTime == 0 || spendTime < minTime) {
            minTime = spendTime;
        }
    }

    public void addErrorMsg(String errorMsg, int httpStatus) {
        if (errorMsg == null || "".equals(errorMsg)) {
            return;
        }
        synchronized (this) {
            String errorId = DigestUtils.md5Hex(instanceId + routeId + errorMsg);
            MonitorErrorMsg monitorErrorMsg = monitorErrorMsgMap.computeIfAbsent(errorId, (k) -> {
                MonitorErrorMsg value = new MonitorErrorMsg();
                value.setErrorId(errorId);
                value.setInstanceId(instanceId);
                value.setRouteId(routeId);
                value.setErrorMsg(errorMsg);
                value.setErrorStatus(httpStatus);
                value.setCount(0);
                return value;
            });
            monitorErrorMsg.setCount(monitorErrorMsg.getCount() + 1);
        }
    }

}

package com.gitee.sop.gatewaycommon.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author tanghc
 */
public class MonitorManager {

    private static Map<String, MonitorData> monitorMap = new ConcurrentHashMap<>(128);

    public Map<String, MonitorData> getMonitorData() {
        return monitorMap;
    }

    public MonitorData getMonitorInfo(String routeId, Function<String, MonitorData> createFun) {
        return monitorMap.computeIfAbsent(routeId, createFun);
    }

}

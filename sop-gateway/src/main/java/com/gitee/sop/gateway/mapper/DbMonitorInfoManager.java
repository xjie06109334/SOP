package com.gitee.sop.gateway.mapper;

import com.gitee.sop.gatewaycommon.monitor.MonitorDTO;
import com.gitee.sop.gatewaycommon.monitor.MonitorErrorMsg;
import com.gitee.sop.gatewaycommon.monitor.RouteErrorCount;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tanghc
 */
@Service
public class DbMonitorInfoManager {

    @Autowired
    private MonitorInfoMapper monitorInfoMapper;

    @Autowired
    private MonitorInfoErrorMapper monitorInfoErrorMapper;

    @Value("${sop.monitor.error-count-capacity:50}")
    int limitCount;

    public void saveMonitorInfoBatch(List<MonitorDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        monitorInfoMapper.saveMonitorInfoBatch(list);
        this.saveMonitorInfoErrorBatch(list);
    }

    private void saveMonitorInfoErrorBatch(List<MonitorDTO> list) {
        List<RouteErrorCount> routeErrorCounts = monitorInfoErrorMapper.listRouteErrorCountAll();
        // 路由id对应的错误次数，key：routeId，value：错误次数
        Map<String, Integer> routeErrorCountsMap = routeErrorCounts.stream()
                .collect(Collectors.toMap(RouteErrorCount::getRouteId, RouteErrorCount::getCount));
        List<MonitorErrorMsg> monitorErrorMsgList = list.stream()
                .filter(monitorDTO -> CollectionUtils.isNotEmpty(monitorDTO.getErrorMsgList()))
                .flatMap(monitorDTO -> {
                    int limit = limitCount - routeErrorCountsMap.getOrDefault(monitorDTO.getRouteId(), 0);
                    // 容量已满
                    if (limit <= 0) {
                        return null;
                    }
                    // 截取剩余
                    return monitorDTO.getErrorMsgList().stream().limit(limit);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(monitorErrorMsgList)) {
            monitorInfoErrorMapper.saveMonitorInfoErrorBatch(monitorErrorMsgList);
        }
    }

}

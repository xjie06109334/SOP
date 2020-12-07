package com.gitee.sop.adminserver.api.service;

import com.gitee.easyopen.annotation.Api;
import com.gitee.easyopen.annotation.ApiService;
import com.gitee.fastmybatis.core.query.Query;
import com.gitee.fastmybatis.core.query.Sort;
import com.gitee.fastmybatis.core.support.PageEasyui;
import com.gitee.fastmybatis.core.util.MapperUtil;
import com.gitee.sop.adminserver.api.service.param.InstanceMonitorSearchParam;
import com.gitee.sop.adminserver.api.service.param.MonitorErrorMsgParam;
import com.gitee.sop.adminserver.api.service.param.MonitorInfoErrorSolveParam;
import com.gitee.sop.adminserver.api.service.param.MonitorSearchParam;
import com.gitee.sop.adminserver.bean.RouteErrorCount;
import com.gitee.sop.adminserver.entity.MonitorInfoError;
import com.gitee.sop.adminserver.entity.MonitorSummary;
import com.gitee.sop.adminserver.mapper.MonitorInfoErrorMapper;
import com.gitee.sop.adminserver.mapper.MonitorInfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 路由监控
 * @author tanghc
 */
@ApiService
public class MonitorNewApi {

    @Autowired
    private MonitorInfoMapper monitorInfoMapper;

    @Autowired
    private MonitorInfoErrorMapper monitorInfoErrorMapper;

    @Api(name = "monitornew.data.page")
    PageInfo<Object> listMonitor(MonitorSearchParam param) {
        Query query = Query.build(param);
        query.orderby("errorCount", Sort.DESC)
                .orderby("avgTime", Sort.DESC);
        return PageHelper.offsetPage(query.getStart(), query.getLimit())
                .doSelectPage(() -> monitorInfoMapper.listMonitorSummary(query))
                .toPageInfo();
    }

    @Api(name = "monitornew.routeid.data.get")
    List<MonitorSummary> listInstanceMonitor(InstanceMonitorSearchParam param) {
        Query query = Query.build(param);
        query.orderby("errorCount", Sort.DESC)
                .orderby("avgTime", Sort.DESC);
        return monitorInfoMapper.listInstanceMonitorInfo(query);
    }

    private Map<String, Integer> getRouteErrorCount() {
        List<RouteErrorCount> routeErrorCounts = monitorInfoErrorMapper.listRouteErrorCount();
        return routeErrorCounts.stream()
                .collect(Collectors.toMap(RouteErrorCount::getRouteId, RouteErrorCount::getCount));
    }

    @Api(name = "monitornew.error.page")
    PageEasyui<MonitorInfoError> listError(MonitorErrorMsgParam param) {
        Query query = param.toQuery()
                .orderby("gmt_modified", Sort.DESC);
        return MapperUtil.queryForEasyuiDatagrid(monitorInfoErrorMapper, query);
    }

    @Api(name = "monitornew.error.solve")
    void solve(MonitorInfoErrorSolveParam param) {
        Query query = Query.build(param);
        Map<String, Object> set = new HashMap<>(4);
        set.put("is_deleted", 1);
        set.put("count", 0);
        monitorInfoErrorMapper.updateByMap(set, query);
    }

}

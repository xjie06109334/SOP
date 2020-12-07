package com.gitee.sop.gateway.mapper;

import com.gitee.fastmybatis.core.mapper.CrudMapper;
import com.gitee.sop.gateway.entity.MonitorInfo;
import com.gitee.sop.gatewaycommon.monitor.MonitorDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * @author tanghc
 */
public interface MonitorInfoMapper extends CrudMapper<MonitorInfo, Long> {

    /**
     * 更新监控状态
     *
     * @return 返回影响行数
     */
    @Update("UPDATE monitor_info " +
            "set max_time=case when max_time < #{maxTime} then #{maxTime} else max_time end " +
            ",min_time=case when min_time > #{minTime} then #{minTime} else min_time end " +
            ",total_request_count=total_request_count + #{totalRequestCount} " +
            ",total_time=total_time + #{totalTime} " +
            ",success_count=success_count + #{successCount} " +
            ",error_count=error_count + #{errorCount} " +
            "where route_id=#{routeId}")
    int updateMonitorInfo(MonitorDTO monitorDTO);


    /**
     * 批量插入监控数据
     * @param list 监控数据
     * @return 返回影响行数
     */
    int saveMonitorInfoBatch(@Param("list") List<MonitorDTO> list);
}

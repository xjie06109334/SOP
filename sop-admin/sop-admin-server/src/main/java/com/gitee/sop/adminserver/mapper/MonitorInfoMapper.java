package com.gitee.sop.adminserver.mapper;

import com.gitee.fastmybatis.core.mapper.CrudMapper;
import com.gitee.fastmybatis.core.query.Query;
import com.gitee.sop.adminserver.entity.MonitorInfo;
import com.gitee.sop.adminserver.entity.MonitorSummary;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author tanghc
 */
public interface MonitorInfoMapper extends CrudMapper<MonitorInfo, Long> {
    List<MonitorSummary> listMonitorSummary(@Param("query") Query query);

    List<MonitorSummary> listInstanceMonitorInfo(@Param("query") Query query);
}

package com.gitee.sop.gateway.mapper;

import com.gitee.fastmybatis.core.mapper.CrudMapper;
import com.gitee.sop.gateway.entity.MonitorInfoError;
import com.gitee.sop.gatewaycommon.monitor.MonitorErrorMsg;
import com.gitee.sop.gatewaycommon.monitor.RouteErrorCount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * @author tanghc
 */
public interface MonitorInfoErrorMapper extends CrudMapper<MonitorInfoError, Long> {

    @Update("UPDATE monitor_info_error " +
            "SET is_deleted=0 " +
            ",count=count + 1 " +
            "WHERE route_id=#{routeId} AND error_id=#{errorId}")
    int updateError(@Param("routeId") String routeId,@Param("errorId")  String errorId);

    int saveMonitorInfoErrorBatch(@Param("list") List<MonitorErrorMsg> list);

    @Select("SELECT route_id routeId, count(*) `count` FROM monitor_info_error \n" +
            "WHERE is_deleted=0 \n" +
            "GROUP BY route_id")
    List<RouteErrorCount> listRouteErrorCount();

    @Select("SELECT route_id routeId, count(*) `count` FROM monitor_info_error \n" +
            "WHERE is_deleted=0 \n" +
            "GROUP BY route_id")
    List<RouteErrorCount> listRouteErrorCountAll();
}

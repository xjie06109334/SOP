package com.gitee.sop.adminserver.mapper;

import com.gitee.fastmybatis.core.mapper.CrudMapper;
import com.gitee.sop.adminserver.bean.RouteErrorCount;
import com.gitee.sop.adminserver.entity.MonitorInfoError;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author tanghc
 */
public interface MonitorInfoErrorMapper extends CrudMapper<MonitorInfoError, Long> {


    @Select("SELECT route_id routeId, count(*) `count` FROM monitor_info_error \n" +
            "WHERE is_deleted=0 \n" +
            "GROUP BY route_id")
    List<RouteErrorCount> listRouteErrorCount();

}

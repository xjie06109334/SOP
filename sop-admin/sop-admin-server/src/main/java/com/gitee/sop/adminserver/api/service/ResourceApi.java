package com.gitee.sop.adminserver.api.service;

import com.gitee.easyopen.annotation.Api;
import com.gitee.easyopen.annotation.ApiService;
import com.gitee.sop.adminserver.api.system.param.IspPageParam;
import com.gitee.sop.adminserver.api.system.param.IspResourceParam;
import com.gitee.sop.adminserver.common.CopyUtil;
import com.gitee.sop.adminserver.entity.IspResource;
import com.gitee.sop.adminserver.mapper.IspResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author tanghc
 */
@ApiService
public class ResourceApi {

    public static final byte RESOURCE_TYPE_SDK = (byte) 0;

    @Autowired
    private IspResourceMapper ispResourceMapper;


    @Api(name = "isp.sdk.list")
    List<IspResource> list(IspPageParam param) {
        return ispResourceMapper.list(param.toQuery());
    }

    @Api(name = "isp.sdk.add")
    void addSdk(IspResourceParam param) {
        IspResource ispResource = CopyUtil.copyBean(param, IspResource::new);
        ispResource.setType(RESOURCE_TYPE_SDK);
        ispResourceMapper.saveIgnoreNull(ispResource);
    }

    @Api(name = "isp.sdk.update")
    void updateSdk(IspResourceParam param) {
        IspResource resource = ispResourceMapper.getById(param.getId());
        CopyUtil.copyProperties(param, resource);
        ispResourceMapper.update(resource);
    }

    @Api(name = "isp.sdk.delete")
    void deleteSdk(@NotNull Long id) {
        IspResource resource = ispResourceMapper.getById(id);
        if (resource != null) {
            ispResourceMapper.deleteById(id);
        }
    }

}

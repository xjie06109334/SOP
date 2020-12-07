package com.gitee.sop.storyweb.controller;

import com.gitee.sop.servercommon.annotation.Open;
import com.gitee.sop.servercommon.bean.OpenContext;
import com.gitee.sop.servercommon.bean.ParamNames;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.storyweb.controller.param.StoryParam;
import com.gitee.sop.storyweb.controller.result.StoryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tanghc
 */
@RestController
@Slf4j
@Api(tags = "故事接口")
public class Example1007_TokenController {

    @ApiOperation(value="传递token", notes = "传递token")
    @Open(value = "story.get.token", needToken = true/* 设置true，网关会校验token是否存在 */)
    @RequestMapping("token")
    public StoryResult token(StoryParam story, HttpServletRequest request) {
        OpenContext openContext = ServiceContext.getCurrentContext().getOpenContext();
        StoryResult result = new StoryResult();
        result.setName("appAuthToken:" + openContext.getAppAuthToken());
        return result;
    }
}

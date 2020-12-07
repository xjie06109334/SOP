package com.gitee.sop.storyweb.controller;

import com.gitee.sop.servercommon.annotation.Open;
import com.gitee.sop.storyweb.controller.result.StoryResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付宝服务端，假设签名验证通过后，到达这里进行具体的业务处理。
 * 这里演示如何接受业务参数。
 * @author tanghc
 */
@RestController
public class Example1005_PermissionController {

    @Open(value = "story.get.permission", permission = true)
    @RequestMapping("perm/get")
    public StoryResult getStory() {
        StoryResult story = new StoryResult();
        story.setId(1L);
        story.setName("海底小纵队(story.get.permission)");
        return story;
    }

}

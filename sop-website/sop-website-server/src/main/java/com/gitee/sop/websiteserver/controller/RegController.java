package com.gitee.sop.websiteserver.controller;

import com.gitee.sop.websiteserver.bean.NoLogin;
import com.gitee.sop.websiteserver.bean.Result;
import com.gitee.sop.websiteserver.bean.Validates;
import com.gitee.sop.websiteserver.controller.param.RegParam;
import com.gitee.sop.websiteserver.entity.UserAccount;
import com.gitee.sop.websiteserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author tanghc
 */
@RestController
@RequestMapping("portal/common")
@NoLogin
public class RegController {

    @Autowired
    private UserService userService;

    @PostMapping("/regIsv")
    public Result reg(@RequestBody @Valid RegParam param) {
        // 3. 校验邮箱是否存在
        this.checkEmail(param.getUsername());
        // 4. 创建用户
        UserAccount userInfo = userService.saveUser(param);
        // 5. 创建ISV
        userService.createIsv(userInfo);
        return Result.ok();
    }

    private void checkEmail(String email) {
        UserAccount username = userService.getUserAccountByUsername(email);
        Validates.isTrue(username != null, "该邮箱已被注册");
    }

}

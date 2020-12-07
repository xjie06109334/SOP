package com.gitee.sop.websiteserver.controller;

import com.gitee.sop.websiteserver.bean.LoginUser;
import com.gitee.sop.websiteserver.bean.NoLogin;
import com.gitee.sop.websiteserver.bean.Result;
import com.gitee.sop.websiteserver.bean.UserContext;
import com.gitee.sop.websiteserver.controller.param.LoginForm;
import com.gitee.sop.websiteserver.controller.result.LoginResult;
import com.gitee.sop.websiteserver.manager.TokenManager;
import com.gitee.sop.websiteserver.service.UserService;
import com.gitee.sop.websiteserver.util.GenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

/**
 * @author tanghc
 */
@RestController
@RequestMapping("portal/common")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("login")
    @NoLogin
    public Result<LoginResult> login(@RequestBody @Valid LoginForm param) {
        String username = param.getUsername();
        String password = param.getPassword();
        password = GenerateUtil.getUserPassword(username, password);
        LoginUser loginUser = userService.getLoginUser(username, password);
        Objects.requireNonNull(loginUser, "用户名密码不正确");
        String token = UUID.randomUUID().toString();
        tokenManager.setUser(token, loginUser);
        LoginResult loginResult = new LoginResult();
        loginResult.setToken(token);
        return Result.ok(loginResult);
    }

    @GetMapping("logout")
    public Result logout() {
        String token = UserContext.getToken();
        tokenManager.removeUser(token);
        return Result.ok();
    }

}

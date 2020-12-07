package com.gitee.sop.websiteserver.manager;

import com.gitee.sop.websiteserver.bean.LoginUser;

/**
 * @author tanghc
 */
public interface TokenManager {

    /**
     * 根据token获取登录用户
     * @param token token
     * @return 返回登录用户，没有返回null
     */
    LoginUser getUser(String token);

    /**
     * 返回token
     * @param token
     * @param user
     * @return
     */
    void setUser(String token, LoginUser user);

    /**
     * 删除用户
     * @param token token
     */
    void removeUser(String token);
}

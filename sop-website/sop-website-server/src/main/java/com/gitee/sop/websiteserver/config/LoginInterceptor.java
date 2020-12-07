package com.gitee.sop.websiteserver.config;

import com.gitee.sop.websiteserver.bean.NoLogin;
import com.gitee.sop.websiteserver.bean.UserContext;
import com.gitee.sop.websiteserver.exception.LoginFailureException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tanghc
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getServletPath().startsWith("/portal")) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        NoLogin noLogin = handlerMethod.getMethodAnnotation(NoLogin.class);
        if (noLogin != null) {
            return true;
        }
        noLogin = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), NoLogin.class);
        if (noLogin != null) {
            return true;
        }
        if (UserContext.getLoginUser(request) == null) {
            throw new LoginFailureException();
        }
        return true;
    }
}

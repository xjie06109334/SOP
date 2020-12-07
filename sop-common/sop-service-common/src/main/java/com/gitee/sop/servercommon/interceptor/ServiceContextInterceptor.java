package com.gitee.sop.servercommon.interceptor;

import com.gitee.sop.servercommon.bean.ServiceContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tanghc
 */
public class ServiceContextInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ServiceContext context = ServiceContext.getCurrentContext();
        context.setRequest(request);
        context.setResponse(response);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ServiceContext.getCurrentContext().unset();
    }
}

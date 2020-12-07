package com.gitee.sop.storyweb.controller;

import com.gitee.sop.servercommon.annotation.Open;
import com.gitee.sop.storyweb.controller.param.GoodsParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 演示参数验证
 * @author tanghc
 */
@RestController
public class Example1004_JSR303Controller {

    @Open("goods.add")
    @RequestMapping("jsr303")
    public Object addGoods(GoodsParam param, HttpServletRequest request) {
        System.out.println(request.getParameter("method"));
        return param;
    }
}

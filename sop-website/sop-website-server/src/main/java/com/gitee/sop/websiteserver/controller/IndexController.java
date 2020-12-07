package com.gitee.sop.websiteserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tanghc
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "redirect:index.html";
    }

}

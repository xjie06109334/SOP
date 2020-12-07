package com.gitee.sop.websiteserver.controller.result;

import lombok.Data;

import java.util.List;

/**
 * @author tanghc
 */
@Data
public class MenuProject {
    private String label;
    private List<MenuModule> children;
}

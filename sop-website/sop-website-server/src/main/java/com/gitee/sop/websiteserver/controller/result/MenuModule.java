package com.gitee.sop.websiteserver.controller.result;

import lombok.Data;

import java.util.List;

/**
 * @author tanghc
 */
@Data
public class MenuModule {
    private String label;
    private List<MenuDocItem> children;
}

package com.gitee.sop.websiteserver.manager;

import com.gitee.sop.websiteserver.bean.DocInfo;
import com.gitee.sop.websiteserver.bean.DocItem;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author tanghc
 */
public interface DocManager {

    void addDocInfo(String serviceId, String docJson, Consumer<DocInfo> callback);

    DocInfo getByTitle(String title);

    DocItem getDocItem(String id);

    Collection<DocInfo> listAll();

    void remove(String serviceId);
}

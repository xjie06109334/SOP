package com.gitee.sop.websiteserver.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.gitee.sop.websiteserver.bean.DocInfo;
import com.gitee.sop.websiteserver.bean.DocItem;
import com.gitee.sop.websiteserver.bean.DocModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author tanghc
 */
@Service
@Slf4j
public class DocManagerImpl implements DocManager {

    // key:title
    private static final Map<String, DocInfo> docDefinitionMap = new HashMap<>();

    /**
     * KEY:serviceId, value: md5
     */
    private static final Map<String, String> serviceIdMd5Map = new HashMap<>();

    // key: DocItem.id
    private static final Map<String, DocItem> ITEM_MAP = new ConcurrentHashMap<>(128);

    private static final DocParser swaggerDocParser = new SwaggerDocParser();

    @Override
    public void addDocInfo(String serviceId, String docInfoJson, Consumer<DocInfo> callback) {
        String newMd5 = DigestUtils.md5DigestAsHex(docInfoJson.getBytes(StandardCharsets.UTF_8));
        String oldMd5 = serviceIdMd5Map.get(serviceId);
        if (Objects.equals(newMd5, oldMd5)) {
            return;
        }
        serviceIdMd5Map.put(serviceId, newMd5);
        JSONObject docRoot = JSON.parseObject(docInfoJson, Feature.OrderedField, Feature.DisableCircularReferenceDetect);
        DocInfo docInfo = swaggerDocParser.parseJson(docRoot);
        docInfo.setServiceId(serviceId);
        docDefinitionMap.put(docInfo.getTitle(), docInfo);
        List<DocModule> docModules = docInfo.getDocModuleList();
        docModules.forEach(docModule -> {
            docModule.getDocItems().forEach(docItem -> {
                ITEM_MAP.put(docItem.getId(), docItem);
            });
        });
        callback.accept(docInfo);
    }

    @Override
    public DocInfo getByTitle(String title) {
        return docDefinitionMap.get(title);
    }

    @Override
    public DocItem getDocItem(String id) {
        return ITEM_MAP.get(id);
    }

    @Override
    public Collection<DocInfo> listAll() {
        return docDefinitionMap.values();
    }

    @Override
    public void remove(String serviceId) {
        serviceIdMd5Map.remove(serviceId);
        docDefinitionMap.entrySet().removeIf(entry -> serviceId.equalsIgnoreCase(entry.getValue().getServiceId()));
    }
}

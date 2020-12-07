package com.gitee.sop.gatewaycommon.param;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 存放上传文件
 * 
 * @author tanghc
 */
public class ApiUploadContext implements UploadContext {

    /**
     * key: 表单name
     */
    private Map<String, List<MultipartFile>> fileMap;
    private List<MultipartFile> allFile;

    public ApiUploadContext(Map<String, List<MultipartFile>> map) {
        if (map == null) {
            map = Collections.emptyMap();
        }
        this.fileMap = map;
        this.allFile = new ArrayList<>();
        map.values().forEach(list -> this.allFile.addAll(list));
    }

    @Override
    public MultipartFile getFile(int index) {
        return this.allFile.get(index);
    }

    @Override
    public List<MultipartFile> getFile(String name) {
        return fileMap.get(name);
    }

    @Override
    public List<MultipartFile> getAllFile() {
        return this.allFile;
    }
}

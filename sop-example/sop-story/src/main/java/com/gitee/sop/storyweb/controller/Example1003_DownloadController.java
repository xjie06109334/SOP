package com.gitee.sop.storyweb.controller;

import com.gitee.sop.servercommon.annotation.Open;
import com.gitee.sop.storyweb.controller.param.StoryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * 演示文件下载
 *
 * @author tanghc
 */
@Api(tags = "文件下载", position = 3)
@Controller
@RequestMapping("download")
public class Example1003_DownloadController {

    @ApiOperation(value = "文件下载", notes = "演示文件下载")
    @Open("file.download")
    @RequestMapping(value = "file1", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE/* 这个一定要加，不然沙箱文档不起作用 */)
    public ResponseEntity<byte[]> download(StoryParam param) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        // 假设下载classpath下的application.properties文件
        ClassPathResource resource = new ClassPathResource("/application.properties");

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", resource.getFilename());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(IOUtils.toByteArray(resource.getInputStream()));
    }
}

package com.project.controller;

import com.project.service.SentinelDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author DingDong
 * @date 2020/4/10
 */
@Slf4j
@RestController
@RequestMapping("/web")
@Api(value = "sentinelDemoController", description = "能力接口")
public class SentinelDemoController {

    @Autowired
    private SentinelDemoService service;

    @GetMapping("/foo")
    @ApiOperation(value = "apiFoo", notes = "hello能力", response = String.class)
    public String apiFoo(@RequestParam(required = false) Long t) throws Exception {
        if (t == null) {
            t = System.currentTimeMillis();
        }
        service.test();
        return service.hello(t);
    }

    @PostMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "upload", notes = "上传文件", response = String.class)
    public String upload(@ApiParam(value = "上传文件", required = true) MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        String filePath = "D:\\IdeaProjects\\study\\assembly-projects-demo\\docs\\" + fileName;
        File localFile = new File(filePath);
        multipartFile.transferTo(localFile);
        return String.format("%s已上传", fileName);
    }

    @GetMapping("/baz/{name}")
    public String apiBaz(@PathVariable("name") String name) {
        return service.helloAnother(name);
    }

}

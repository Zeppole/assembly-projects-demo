package com.project.controller;

import com.google.common.base.Preconditions;
import com.project.service.SentinelDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DingDong
 * @date 2020/4/10
 */
@Slf4j
@RestController
@RequestMapping("/thin")
public class SentinelDemoController {

    @Autowired
    private SentinelDemoService service;

    @GetMapping("/baz/{name}")
    public String apiBaz(@PathVariable("name") String name) {
        return service.helloAnother(name);
    }

    @GetMapping("/guava")
    public String guava() {
        Object obj = new Object();
        Preconditions.checkNotNull(obj, "is null");
        return "judge is null:" + obj.toString();
    }

}

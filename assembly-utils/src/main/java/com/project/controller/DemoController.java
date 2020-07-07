package com.project.controller;

import com.google.common.base.Preconditions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DingDong
 * @date 2020/7/6
 */
@RequestMapping("/utils")
@RestController
public class DemoController {

    @RequestMapping("dd")
    public String demo() {
        return "dd";
    }

    @GetMapping("/guava")
    public String guava() {
        Object obj = new Object();
        Preconditions.checkNotNull(obj, "is null");
        return "judge is null:" + obj.toString();
    }
}

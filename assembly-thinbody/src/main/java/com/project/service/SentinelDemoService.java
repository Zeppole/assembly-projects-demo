package com.project.service;

import org.springframework.stereotype.Service;

/**
 * @author DingDong
 * @date 2020/4/10
 */
@Service
public class SentinelDemoService {

    public void test() {
        System.out.println("Test");
    }

    public String hello(long s) {
        if (s < 0) {
            throw new IllegalArgumentException("invalid arg");
        }
        return String.format("Hello at %d", s);
    }

    public String helloAnother(String name) {
        if (name == null || "bad".equals(name)) {
            throw new IllegalArgumentException("oops");
        }
        if ("foo".equals(name)) {
            throw new IllegalStateException("oops");
        }
        return "Hello, " + name;
    }

    public String helloFallback(long s, Throwable ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at " + s;
    }

    public String defaultFallback() {
        System.out.println(" Go to default fallback");
        return "default_fallback";
    }


}

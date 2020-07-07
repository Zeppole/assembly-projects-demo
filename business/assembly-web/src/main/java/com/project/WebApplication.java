package com.project;

import com.project.utils.ArgsUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

/**
 * @author 161220a
 */
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        Properties properties = ArgsUtils.formatArgs(args);
        System.out.println(properties);
        SpringApplication.run(WebApplication.class, args);
    }
}

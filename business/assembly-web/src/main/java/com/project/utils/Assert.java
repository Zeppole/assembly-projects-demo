package com.project.utils;

import java.util.Objects;

/**
 * @author DingDong
 * @date 2020/5/7
 */
public interface Assert {

    /**
     * 非空判断
     *
     * @param obj 参数
     */
    static void notNull(Object obj) {
        if (Objects.isNull(obj)) {
            throw new RuntimeException("it must not be null");
        }
    }
}

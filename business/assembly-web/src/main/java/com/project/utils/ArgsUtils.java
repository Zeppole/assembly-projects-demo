package com.project.utils;

import java.util.Properties;

/**
 * @author DingDong
 * @date 2020/4/13
 */
public class ArgsUtils {

    public static Properties formatArgs(String[] args) {
        Properties properties = new Properties();
        for (String str : args) {
            if (str.startsWith("-D") || str.startsWith("--")) {
                str = str.substring(2);
                String[] pros = str.split("=");
                properties.put(pros[0], pros[1]);
            }
        }
        return properties;
    }

    public static void main(String[] args) {
        Father father = new Son();
        char c = 'a';
        father.f1(c);
    }

}

class Father {
    public void f1() {
        System.out.println("Father.f1()");
    }

    public void f1(int i) {
        System.out.println("Father.f1(int)" + i);
        ArgsUtils.main(null);
    }
}

class Son extends Father {
    @Override
    public void f1() {
        System.out.println("Son.f1()");
    }

    public void f1(char c) {
        System.out.println("Son.f1(char)" + c);
    }
//    @Override
//    public void f1(int i) {
//        System.out.println("Son.f1(int)" + i);
//    }
}
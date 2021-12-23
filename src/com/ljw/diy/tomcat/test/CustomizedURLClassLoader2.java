package com.ljw.diy.tomcat.test;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 在一个类加载器下，一个类只有一个对应的类对象。如果有多个类加载器都各自加载了同一个类，那么他们
 * 将得到不同的类对象。
 */
public class CustomizedURLClassLoader2 extends URLClassLoader {
    public CustomizedURLClassLoader2(URL[] urls) {
        super(urls);
    }

    public static void main(String[] args) throws Exception {
        URL url = new URL("file:/Users/liangjianwen/IdeaProjects/diy-tomcat/jar_4_test/test.jar");
        URL[] urls = new URL[] {url};

        CustomizedURLClassLoader2 loader1 = new CustomizedURLClassLoader2(urls);
        Class<?> how2jClass0 = loader1.loadClass("com.ljw.diy.tomcat.test.HOW2J");
        Class<?> how2jClass1 = loader1.loadClass("com.ljw.diy.tomcat.test.HOW2J");

        CustomizedURLClassLoader2 loader2 = new CustomizedURLClassLoader2(urls);
        Class<?> how2jClass2 = loader2.loadClass("com.ljw.diy.tomcat.test.HOW2J");

        System.out.println(how2jClass0 == how2jClass1);
        System.out.println(how2jClass1 == how2jClass2);
    }
}

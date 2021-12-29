package com.ljw.diy.tomcat;

import com.ljw.diy.tomcat.classloader.CommonClassLoader;

import java.lang.reflect.Method;

public class Bootstrap {

    public static void main(String[] args) throws Exception {
        CommonClassLoader commonClassLoader = new CommonClassLoader();

        Thread.currentThread().setContextClassLoader(commonClassLoader);

        String serverClassName = "com.ljw.diy.tomcat.catalina.Server";

        Class<?> serverClazz = commonClassLoader.loadClass(serverClassName);

        Object serverObject = serverClazz.newInstance();

        Method m = serverClazz.getMethod("start");

        m.invoke(serverObject);

        System.out.println(serverClazz.getClassLoader());
    }
}

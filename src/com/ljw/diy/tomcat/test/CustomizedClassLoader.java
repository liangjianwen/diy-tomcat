package com.ljw.diy.tomcat.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.lang.reflect.Method;

public class CustomizedClassLoader extends ClassLoader {
    private File classesFolder = new File(System.getProperty("user.dir"), "classes_4_test");

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = loadClassData(name);
        return defineClass(name, data, 0, data.length);
    }

    private byte[] loadClassData(String fullQualifiedName) throws ClassNotFoundException {
        String fileName = StrUtil.replace(fullQualifiedName,".", "/") + ".class";
        File classFile = new File(classesFolder, fileName);
        if (!classFile.exists()){
            throw new ClassNotFoundException(fullQualifiedName);
        }

        return FileUtil.readBytes(classFile);
    }

    public static void main(String[] args) throws Exception {
        CustomizedClassLoader loader = new CustomizedClassLoader();

        Class<?> how2jClass = loader.loadClass("com.ljw.diy.tomcat.test.HOW2J");

        Object o = how2jClass.newInstance();

        Method m = how2jClass.getMethod("hello");

        m.invoke(o);

        System.out.println(how2jClass.getClassLoader());
    }
}

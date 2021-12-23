package com.ljw.diy.tomcat.test;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 双亲委派机制：
 * 我们在使用CustomizedURLClassLoader加载com.ljw.diy.tomcat.test.HOW2J类的时候，它并不是
 * 直接从CustomizedURLClassLoader里去加载。而是先从最上面的BootstrapClassLoader里去找，如果
 * 没有就从ExtensionClassLoader里去找，如果还是没有，就从ApplicationClassLoader里去找，如果
 * 没有，才最后到CustomizedURLClassLoader里去找。
 *
 * 双亲委派机制的意义：
 * 双亲委派机制的意义主要是保护一些基本类不受影响。
 * 比如常用的String类，其全限定名是java.lang.String，只是java.lang这个包下的类在使用的时候，可以
 * 不用import而直接使用。像这种基本类按照双亲委派机制都应该从rt.jar里去获取，而不应该从自定义加载器里
 * 去获取某个开发人员自己写的java.lang.String，毕竟开发人员自己写的java.lang.String可能有很多bug,
 * 通过这种方式，无论如何大家使用的都是rt.jar里的java.lang.String类了。
 *
 * 双亲委派机制里的"双"是什么意思？
 * 英文全称：Parents Delegation Model
 * parents本意是想表达的意思是上溯，父辈，祖先的意思。
 * 翻译成双亲不够准确。
 */
public class CustomizedURLClassLoader extends URLClassLoader {
    public CustomizedURLClassLoader(URL[] urls) {
        super(urls);
    }

    public static void main(String[] args) throws Exception {
        URL url = new URL("file:/Users/liangjianwen/IdeaProjects/diy-tomcat/jar_4_test/test.jar");
        URL[] urls = new URL[] {url};

        CustomizedURLClassLoader loader = new CustomizedURLClassLoader(urls);

        Class<?> how2jClass = loader.loadClass("com.ljw.diy.tomcat.test.HOW2J");

        Object o = how2jClass.newInstance();
        Method m = how2jClass.getMethod("hello");
        m.invoke(o);

        System.out.println(how2jClass.getClassLoader());
    }
}

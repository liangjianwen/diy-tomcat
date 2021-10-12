package com.ljw.diy.tomcat.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.ljw.diy.tomcat.util.MiniBrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestTomcat {
    private static int port = 18080;
    private static String ip = "127.0.0.1";

    @BeforeClass
    public static void beforeClass(){
        if (NetUtil.isUsableLocalPort(port)){
            System.err.println("请先启动 位于端口：" +port+ "的 diy tomcat，否则无法进行单元测试");
            System.exit(1);
        } else {
            System.out.println("检测到 diy tomcat已经启动，开始单元测试");
        }
    }

    @Test
    public void testHelloTomcat(){
        String html = getContentString("/");
        Assert.assertEquals(html, "Hello DIY Tomcat from ljw");
    }

    @Test
    public void testHtml(){
        String html = getContentString("/a.html");
        Assert.assertEquals(html, "Hello DIY Tomcat from ljw");
    }

    @Test
    public void testTimeConsumeHtml() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 20, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10));

        TimeInterval timeInterval = DateUtil.timer();
        for (int i = 0; i < 3; i++){
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    getContentString("/timeConsume.html");
                }
            });
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1, TimeUnit.HOURS);

        long duration = timeInterval.intervalMs();
        System.out.println("duration:"+duration);

        Assert.assertTrue(duration > 3000);
    }

    @Test
    public void testAIndex(){
        String html = getContentString("/a/index.html");
        Assert.assertEquals(html, "Hello DIY Tomcat from index.html@a");
    }

    private String getContentString(String uri){
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        String content = MiniBrowser.getContentString(url);
        return content;
    }
}
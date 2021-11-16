package com.ljw.diy.tomcat.catalina;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.ljw.diy.tomcat.http.Request;
import com.ljw.diy.tomcat.http.Response;
import com.ljw.diy.tomcat.util.Constant;
import com.ljw.diy.tomcat.util.ThreadPoolUtil;
import com.ljw.diy.tomcat.util.WebXMLUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Server {
    private Service service;

    public Server(){
        this.service = new Service(this);
    }

    public void start(){
        TimeInterval timeInterval = DateUtil.timer();
        logJvm();
        init();
        LogFactory.get().info("Server startup in {} ms", timeInterval.intervalMs());
    }

    private void init(){
        service.start();
    }

    private static void logJvm(){
        Map<String, String> infos = new LinkedHashMap<>();
        infos.put("Server version", "Ljw DiyTomcat/1.0.1");
        infos.put("Server built", "2021-09-25 10:00:00");
        infos.put("Server number", "1.0.1");
        infos.put("OS Name\t", SystemUtil.get("os.name"));
        infos.put("OS Version", SystemUtil.get("os.version"));
        infos.put("Architecture", SystemUtil.get("os.arch"));
        infos.put("Java Home", SystemUtil.get("java.home"));
        infos.put("JVM Version", SystemUtil.get("java.runtime.version"));
        infos.put("JVM Vendor", SystemUtil.get("java.vm.specification.vendor"));

        Set<String> keys = infos.keySet();
        for (String key : keys){
            LogFactory.get().info(key+":\t\t"+infos.get(key));
        }
    }

}

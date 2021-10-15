package com.ljw.diy.tomcat.catalina;

import com.ljw.diy.tomcat.util.ServerXMLUtil;
import java.util.List;

public class Engine {
    private String defaultHost;
    private List<Host> hosts;
    private Service service;

    public Engine(Service service){
        this.service = service;
        this.defaultHost = ServerXMLUtil.getEngineDefaultHost();
        this.hosts = ServerXMLUtil.getHosts(this);
        checkDefault();
    }

    private void checkDefault(){
        if (null == getDefaultHost()){
            throw new RuntimeException("the default host "+defaultHost+" dose not exist!");
        }
    }

    public Host getDefaultHost(){
        for (Host host : hosts){
            if (host.getName().equals(defaultHost)){
                return host;
            }
        }
        return null;
    }
}

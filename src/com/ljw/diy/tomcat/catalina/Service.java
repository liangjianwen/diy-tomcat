package com.ljw.diy.tomcat.catalina;

import com.ljw.diy.tomcat.util.ServerXMLUtil;

public class Service {
    private String name;
    private Engine engine;

    public Service(){
        this.name = ServerXMLUtil.getServiceName();
        this.engine = new Engine(this);
    }

    public Engine getEngine(){
        return engine;
    }
}

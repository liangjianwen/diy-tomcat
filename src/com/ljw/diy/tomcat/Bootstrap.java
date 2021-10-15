package com.ljw.diy.tomcat;

import com.ljw.diy.tomcat.catalina.Server;

public class Bootstrap {

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}

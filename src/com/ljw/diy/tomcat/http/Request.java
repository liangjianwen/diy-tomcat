package com.ljw.diy.tomcat.http;

import cn.hutool.core.util.StrUtil;
import com.ljw.diy.tomcat.Bootstrap;
import com.ljw.diy.tomcat.catalina.Context;
import com.ljw.diy.tomcat.catalina.Engine;
import com.ljw.diy.tomcat.catalina.Host;
import com.ljw.diy.tomcat.catalina.Service;
import com.ljw.diy.tomcat.util.MiniBrowser;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Request {
    /**
     * 比如请求是：http://127.0.0.1:18080/index.html?name=gareen
     * 那么http请求就会是：
     * GET /index.html?name=gareen HTTP/1.1
     * Host: 127.0.0.1:8080
     * Connection: keep-alive
     *
     * 请求的uri就是第一行刚开始的两个空格之间的数据：
     * /index.html?name=gareen
     *
     * 然后再考虑是否有参数，带了问号就表示有参数，那么对有参数和没参数分别处理一下，就拿到了uri
     */
    private String requestString;//表示请求的字符串
    private String uri;//请求的uri
    private Socket socket;
    private Context context;
    private Service service;

    public Request(Socket socket, Service service) throws IOException {
        //创建Request对象用来解析requestString和uri
        this.socket = socket;
        this.service = service;
        parseHttpRequest();
        if (StrUtil.isEmpty(requestString)){
            return;
        }
        parseUri();
        parseContext();
        if (!"/".equals(context.getPath())){
            uri = StrUtil.removePrefix(uri, context.getPath());
        }
    }

    private void parseContext(){
        String path = StrUtil.subBetween(uri, "/", "/");
        if (null == path){
            path = "/";
        }else {
            path = "/" + path;
        }

        Engine engine = service.getEngine();

        context = engine.getDefaultHost().getContext(path);
        if (null == context){
            context = engine.getDefaultHost().getContext("/");
        }
    }

    private void parseHttpRequest() throws IOException {
        InputStream is = this.socket.getInputStream();
        byte[] bytes = MiniBrowser.readBytes(is);
        requestString = new String(bytes, "utf-8");
    }

    private void parseUri(){
        String temp = StrUtil.subBetween(requestString, " ", " ");
        if (!StrUtil.contains(temp, '?')){
            uri = temp;
            return;
        }
        temp = StrUtil.subBefore(temp, '?', false);
        uri = temp;
    }

    public String getUri(){
        return uri;
    }

    public String getRequestString(){
        return requestString;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

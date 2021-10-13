package com.ljw.diy.tomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.ljw.diy.tomcat.catalina.Context;
import com.ljw.diy.tomcat.catalina.Host;
import com.ljw.diy.tomcat.http.Request;
import com.ljw.diy.tomcat.http.Response;
import com.ljw.diy.tomcat.util.Constant;
import com.ljw.diy.tomcat.util.ThreadPoolUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Bootstrap {

    public static void main(String[] args) {
        try {
            logJvm();

            Host host = new Host();
            int port = 18080;

            ServerSocket ss = new ServerSocket(port);

            while (true){
                Socket s = ss.accept();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request request = new Request(s, host);
                            System.out.println("浏览器的输入信息： \r\n" + request.getRequestString());
                            System.out.println("uri:" + request.getUri());

                            Response response = new Response();

                            String uri = request.getUri();
                            if (null == uri)
                                return;

                            Context context = request.getContext();

                            System.out.println(uri);
                            if ("/".equals(uri)){
                                String html = "Hello DIY Tomcat from ljw";
                                response.getWriter().println(html);
                            }else {
                                //a.html -> uri /a.html,   fileName: a.html
                                String fileName = StrUtil.removePrefix(uri, "/");
                                File file = FileUtil.file(context.getDocBase(), fileName);
                                if (file.exists()){
                                    String fileContent = FileUtil.readUtf8String(file);
                                    response.getWriter().println(fileContent);

                                    if (fileName.equals("timeConsume.html")){
                                        ThreadUtil.sleep(1000);//1 Sec
                                    }
                                }else {
                                    response.getWriter().println("File Not Found");
                                }
                            }

                            handle200(s, response);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                ThreadPoolUtil.run(r);
            }
        }catch (IOException e){
            LogFactory.get().error(e);
            e.printStackTrace();
        }
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

    private static void handle200(Socket s, Response response) throws IOException {
        String contentType = response.getContentType(); // text/html
        String headText = Constant.response_head_200;
        headText = StrUtil.format(headText, contentType);
        byte[] head = headText.getBytes();

        byte[] body = response.getBody();

        byte[] responseBytes = new byte[head.length + body.length];
        ArrayUtil.copy(head, 0, responseBytes, 0, head.length);
        ArrayUtil.copy(body, 0, responseBytes, head.length, body.length);

        OutputStream os = s.getOutputStream();
        os.write(responseBytes);
        s.close();
    }
}

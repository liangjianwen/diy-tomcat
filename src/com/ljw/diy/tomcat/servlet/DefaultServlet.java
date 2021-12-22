package com.ljw.diy.tomcat.servlet;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.ljw.diy.tomcat.catalina.Context;
import com.ljw.diy.tomcat.http.Request;
import com.ljw.diy.tomcat.http.Response;
import com.ljw.diy.tomcat.util.Constant;
import com.ljw.diy.tomcat.util.WebXMLUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class DefaultServlet extends HttpServlet {
    private static DefaultServlet instance = new DefaultServlet();

    public static synchronized DefaultServlet getInstance() {
        return instance;
    }

    public DefaultServlet() {
    }

    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Request request = (Request) httpServletRequest;
        Response response = (Response) httpServletResponse;

        Context context = request.getContext();

        String uri = request.getUri();
        if ("/500.html".equals(uri)){
            throw new RuntimeException("this is a deliberately created exception");
        }

        if ("/".equals(uri)){
            uri = WebXMLUtil.getWelcomeFile(request.getContext());
        }

        String fileName = StrUtil.removePrefix(uri, "/");
        File file = FileUtil.file(context.getDocBase(), fileName);

        if (file.exists()){
            String extName = FileUtil.extName(file);
            String mimeType = WebXMLUtil.getMimeType(extName);
            response.setContentType(mimeType);

            byte body[] = FileUtil.readBytes(file);
            response.setBody(body);

            if (fileName.equals("timeConsume.html")){
                ThreadUtil.sleep(1000);
            }

            response.setStatus(Constant.CODE_200);
        } else {
            response.setStatus(Constant.CODE_404);
        }
    }
}

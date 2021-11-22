package com.ljw.diy.tomcat.servlet;

import cn.hutool.core.util.ReflectUtil;
import com.ljw.diy.tomcat.catalina.Context;
import com.ljw.diy.tomcat.http.Request;
import com.ljw.diy.tomcat.http.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InvokerServlet extends HttpServlet {
    private static InvokerServlet instance = new InvokerServlet();

    public static synchronized InvokerServlet getInstance() {
        return instance;
    }

    private InvokerServlet(){

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Request request = (Request) req;
        Response response = (Response) resp;

        String uri = request.getUri();
        Context context = request.getContext();
        String servletClassName = context.getServletClassName(uri);

        Object servletObject = ReflectUtil.newInstance(servletClassName);
        ReflectUtil.invoke(servletObject, "service", request, response);
    }
}

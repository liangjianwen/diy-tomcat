package com.ljw.diy.tomcat.catalina;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import com.ljw.diy.tomcat.http.Request;
import com.ljw.diy.tomcat.http.Response;
import com.ljw.diy.tomcat.servlet.InvokerServlet;
import com.ljw.diy.tomcat.util.Constant;
import com.ljw.diy.tomcat.util.WebXMLUtil;
import com.ljw.diy.tomcat.webappservlet.HelloServlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpProcessor {
    public void execute(Socket s, Request request, Response response){
        try {
            String uri = request.getUri();
            if (null == uri)
                return;
            System.out.println(uri);

            Context context = request.getContext();
            String servletClassName = context.getServletClassName(uri);

            if (servletClassName!=null){
                InvokerServlet.getInstance().service(request, response);
            } else {
                if ("/500.html".equals(uri)){
                    throw new Exception("this is a deliberately created exception");
                } else {
                    if ("/".equals(uri)){
                        uri = WebXMLUtil.getWelcomeFile(request.getContext());
                    }

                    //a.html -> uri /a.html,   fileName: a.html
                    String fileName = StrUtil.removePrefix(uri, "/");
                    File file = FileUtil.file(context.getDocBase(), fileName);

                    if (file.exists()){
                        String extName = FileUtil.extName(file);
                        String mimeType = WebXMLUtil.getMimeType(extName);
                        response.setContentType(mimeType);

                        //String fileContent = FileUtil.readUtf8String(file);
                        //response.getWriter().println(fileContent);

                        byte body[] = FileUtil.readBytes(file);
                        response.setBody(body);

                        if (fileName.equals("timeConsume.html")){
                            ThreadUtil.sleep(1000);//1 Sec
                        }
                    }else {
                        handle404(s, uri);
                        return;
                    }
                }
            }
            handle200(s, response);
        }catch (Exception e){
            LogFactory.get().error(e);
            handle500(s, e);
        }finally {
            try {
                if (!s.isClosed()){
                    s.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
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
    }

    protected void handle404(Socket socket, String uri) throws IOException {
        OutputStream os = socket.getOutputStream();
        String responseText = StrUtil.format(Constant.textFormat_404, uri, uri);
        responseText = Constant.response_head_404 + responseText;
        byte[] responseByte = responseText.getBytes("utf-8");
        os.write(responseByte);
    }

    protected void handle500(Socket s, Exception e){
        try {
            OutputStream os = s.getOutputStream();
            StackTraceElement stes[] = e.getStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append(e.toString());
            sb.append("\r\n");
            for (StackTraceElement ste : stes){
                sb.append("\t");
                sb.append(ste.toString());
                sb.append("\r\n");
            }

            String msg = e.getMessage();

            if (null != msg && msg.length()>20){
                msg = msg.substring(0, 19);
            }

            String text = StrUtil.format(Constant.textFormat_500, msg, e.toString(), sb.toString());
            text = Constant.response_head_500 + text;
            byte[] responseBytes = text.getBytes(StandardCharsets.UTF_8);
            os.write(responseBytes);
        } catch (IOException e1){
            e1.printStackTrace();
        }
    }
}

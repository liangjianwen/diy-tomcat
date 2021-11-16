package com.ljw.diy.tomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

public class Response {
    private StringWriter stringWriter;
    private PrintWriter writer;
    private String contentType;
    public Response(){
        this.stringWriter = new StringWriter();
        this.writer = new PrintWriter(stringWriter);
        this.contentType = "text/html";
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBody() throws UnsupportedEncodingException{
        String content = stringWriter.toString();
        byte[] body = content.getBytes("utf-8");
        return body;
    }
}

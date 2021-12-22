package com.ljw.diy.tomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Response extends BaseResponse {
    private StringWriter stringWriter;
    private PrintWriter writer;
    private String contentType;
    private byte[] body;
    private int status;
    public Response(){
        this.stringWriter = new StringWriter();
        this.writer = new PrintWriter(stringWriter);
        this.contentType = "text/html";
    }

    public byte[] getBody() throws UnsupportedEncodingException{
        if (body==null){
            String content = stringWriter.toString();
            body = content.getBytes(StandardCharsets.UTF_8);
        }
        return body;
    }

    public void setBody(byte[] body){
        this.body = body;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
    }
}

package com.ljw.diy.tomcat.util;

import cn.hutool.core.io.FileUtil;
import com.ljw.diy.tomcat.catalina.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

import static com.ljw.diy.tomcat.util.Constant.webXmlFile;

public class WebXMLUtil {
    public static String getWelcomeFile(Context context){
        String xml = FileUtil.readUtf8String(webXmlFile);
        Document d = Jsoup.parse(xml);
        Elements es = d.select("welcome-file");
        for (Element e : es){
            String welcomeFileName = e.text();
            File f = new File(context.getDocBase(), welcomeFileName);
            if (f.exists()){
                return f.getName();
            }
        }
        return "index.html";
    }
}

package com.ljw.diy.tomcat.classloader;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class WebappClassLoader extends URLClassLoader {
    public WebappClassLoader(String docBase, ClassLoader commonClassLoader) {
        super(new URL[]{}, commonClassLoader);

        try {
            File webinfFolder = new File(docBase, "WEB-INF");
            File classesFolder = new File(webinfFolder, "classes");
            File libFolder = new File(webinfFolder, "lib");

            URL url = new URL("file:" + classesFolder.getAbsolutePath() + "/");
            this.addURL(url);
            List<File> jarFiles = FileUtil.loopFiles(libFolder);
            for (File file : jarFiles) {
                url = new URL("file:" + file.getAbsolutePath());
                this.addURL(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

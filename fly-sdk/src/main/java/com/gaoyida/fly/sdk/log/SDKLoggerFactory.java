package com.gaoyida.fly.sdk.log;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * @author gaoyida
 * @date 2019/10/22 下午2:02
 */
public class SDKLoggerFactory {

    public static int FILE_COUNT = 2;

    public static int FILE_SIZE = 1024 * 1024 * 20;  //20M

    public static FileHandler fileHandler;

    public static void init(FileHandler fileHandler) {
        SDKLoggerFactory.fileHandler = fileHandler;
    }

    public static Logger getLogger(Class c) {

        Logger logger = Logger.getLogger(c.getName());
        if (fileHandler != null) {
            logger.addHandler(fileHandler);
        }
        logger.setUseParentHandlers(false);

        return logger;
    }

    //日志路径
    // /User/FLY/APPNAME/INSTANCEID/
    public static FileHandler initFileHandler(String path, String appName, String instance) {
        String homePath = null;

        if (path != null) {
            File f = new File(path.trim());
            if (f.isDirectory() && f.canWrite()) {
                homePath = path;
            }
        }

        if (homePath == null) {
            homePath = (String) System.getProperties().get("user.home");
            if (homePath == null) {
                homePath = "/tmp";
            }
        }

        String folder = homePath + File.separator + "fly";

        String filepath;

        if (appName == null) {
            filepath = folder + File.separator + "unknown_app.log";
        } else {
            if (instance == null || instance.equals("default")) {
                filepath = folder + File.separator + appName + ".log";
            } else {
                filepath = folder + File.separator + appName + "-" + instance + ".log";
            }
        }

        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }

        try {
            FileHandler fileHandler = new FileHandler(filepath, FILE_SIZE, FILE_COUNT);
            fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
            init(fileHandler);
            return fileHandler;
        } catch (Exception e) {
            throw new RuntimeException("init fileHandler occur error" + e.getMessage());
        }

    }
}

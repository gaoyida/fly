package com.gaoyida.fly.sdk;

import com.gaoyida.fly.sdk.log.SDKLoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author gaoyida
 * @date 2019/10/22 下午4:25
 */
public class SdkConfig {

    public static final String propertiesPath = "flysdk.properties";
    private static Logger logger = SDKLoggerFactory.getLogger(SdkConfig.class);
    private static List<String> serverAddressList = new ArrayList<String>();

    public static boolean init(Properties properties) {

        String servers = (String) properties.getProperty("serverAddressList");

        if (servers != null) {
            try {
                String[] addrs = servers.split(",");
                serverAddressList.clear();
                for (String addr : addrs) {
                    serverAddressList.add(addr.trim());
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "init server address occur error:" + servers, e);
            }
        }
        return true;
    }

    public static List<String> getServerAddressList() {
        return serverAddressList;
    }
}

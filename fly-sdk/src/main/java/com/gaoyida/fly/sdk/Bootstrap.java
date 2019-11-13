package com.gaoyida.fly.sdk;

import com.gaoyida.fly.sdk.log.SDKLoggerFactory;
import com.gaoyida.fly.sdk.net.FlyBioInvokeService;
import com.gaoyida.fly.sdk.protocol.GetIDResponse;
import com.gaoyida.fly.sdk.protocol.GetSeqIDRequest;
import com.gaoyida.fly.sdk.protocol.GetUIDRequest;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author gaoyida
 * @date 2019/10/22 下午2:01
 */
public class Bootstrap {

    public static void main(String[] args) throws Exception {
        try {
            Properties properties = new Properties();
            properties.load(Bootstrap.class.getClassLoader().getResourceAsStream(SdkConfig.propertiesPath));
            String appName = properties.getProperty("appName");
            String instance = properties.getProperty("instance");
            SDKLoggerFactory.initFileHandler(null, appName, instance);

            Logger logger = SDKLoggerFactory.getLogger(Bootstrap.class);
            logger.log(Level.INFO, "initializing...");
            boolean init = SdkConfig.init(properties);
            if (!init) {
                logger.log(Level.SEVERE, "initializing error");
                System.exit(-1);
            }

            FlyBioInvokeService flyBioInvokeService = new FlyBioInvokeService();
            GetUIDRequest req = new GetUIDRequest();

            GetIDResponse response = flyBioInvokeService.send(req, GetIDResponse.class);
            System.out.println(response.getId());

            GetSeqIDRequest req1 = new GetSeqIDRequest();
            response = flyBioInvokeService.send(req1, GetIDResponse.class);
            System.out.println(response.getId());

            System.in.read();
        } catch (Exception e) {
            throw e;
        }

    }
}

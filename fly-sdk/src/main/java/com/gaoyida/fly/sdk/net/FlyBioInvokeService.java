package com.gaoyida.fly.sdk.net;

import com.gaoyida.fly.common.Constant;
import com.gaoyida.fly.sdk.SdkConfig;
import com.gaoyida.fly.sdk.exception.ConnectionException;
import com.gaoyida.fly.sdk.exception.SDKInitializationException;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * @author gaoyida
 * @date 2019/10/22 下午4:25
 */
public class FlyBioInvokeService extends AbstractInvokeService {


    @Override
    protected BioClient createConnect() {
        List<String> serverAddressList = SdkConfig.getServerAddressList();
        if (serverAddressList == null || serverAddressList.isEmpty()) {
            throw new SDKInitializationException("no available server found.");
        }

        Collections.shuffle(serverAddressList);
        for (String ip : serverAddressList) {
            try {
                return connect(ip, Constant.LISTEN_PORT);
            } catch (Exception e) {
                logger.log(Level.WARNING, "failed to connect to " + ip + ", exception:" + e.getMessage());
            }
        }
        throw new ConnectionException("failed to connect to server.");

    }

    public BioClient connect(String ip, int port) throws Exception {
        return BioClient.connect(ip, port);
    }
}

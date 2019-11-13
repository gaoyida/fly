package com.gaoyida.fly.sdk.net;

import com.gaoyida.fly.sdk.log.SDKLoggerFactory;
import com.gaoyida.fly.sdk.protocol.RequestBase;
import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * @author gaoyida
 * @date 2019/10/22 下午2:48
 */
public abstract class AbstractInvokeService {

    public static final Logger logger = SDKLoggerFactory.getLogger(AbstractInvokeService.class);
    private static final String UTF8 = "UTF-8";
    public static AtomicLong MSGID = new AtomicLong(0);
    public static Gson gson = new Gson();
    private Client client;

    public <T> T send(RequestBase request, Class<T> responseType) throws Exception {
        Client client = getRequestClient();
        return client.send(request, responseType);
    }

    protected abstract Client createConnect() throws Exception;


    public Client getRequestClient() throws Exception {
        if (client == null || !client.check()) {
            client = createConnect();
        }
        return client;
    }
}
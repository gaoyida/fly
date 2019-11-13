package com.gaoyida.fly.sdk.net;

import com.gaoyida.fly.sdk.protocol.RequestBase;

/**
 * @author gaoyida
 * @date 2019/10/30 下午9:44
 */
public interface Client {
    boolean check();

    <T> T send(RequestBase request, Class<T> responseType) throws Exception;

}

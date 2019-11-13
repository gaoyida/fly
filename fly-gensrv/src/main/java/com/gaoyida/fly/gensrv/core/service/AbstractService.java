package com.gaoyida.fly.gensrv.core.service;

/**
 * @author gaoyida
 * @date 2019/10/25 下午1:28
 */
public abstract class AbstractService {

    public abstract void startService() throws Exception;

    public abstract void shutdownService() throws Exception;

}

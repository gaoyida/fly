package com.gaoyida.fly.common;

import java.nio.charset.Charset;

/**
 * @author gaoyida
 * @date 2019/9/29 下午5:10
 */
public abstract class Constant {

    public static final int LISTEN_PORT = 8888;

    public static final int CODE_NOT_AVAILABLE = 503;
    public static final int CODE_ERR = 500;
    public static final int CODE_OK = 200;
    public static final int CODE_FORWARD = 302;

    public static final int GAP = 5000;

    public static final Charset UTF8 = Charset.forName("utf-8");

    public static final int forwardConnectTimeout = 3000;

}

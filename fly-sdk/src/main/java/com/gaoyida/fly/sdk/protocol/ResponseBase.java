package com.gaoyida.fly.sdk.protocol;

/**
 * @author gaoyida
 * @date 2019/10/22 下午5:06
 */
public class ResponseBase {
    protected int code;
    protected String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

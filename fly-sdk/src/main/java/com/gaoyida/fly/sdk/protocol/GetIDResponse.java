package com.gaoyida.fly.sdk.protocol;

/**
 * @author gaoyida
 * @date 2019/10/22 下午5:06
 */
public class GetIDResponse extends ResponseBase {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

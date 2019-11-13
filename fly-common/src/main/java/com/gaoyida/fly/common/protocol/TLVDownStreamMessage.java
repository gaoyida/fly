package com.gaoyida.fly.common.protocol;

import com.google.gson.Gson;

import static com.gaoyida.fly.common.Constant.UTF8;

/**
 * @author gaoyida
 * @date 2019/10/22 上午11:43
 */
public abstract class TLVDownStreamMessage extends TLVMessage {
    private boolean feedBack = true;

    public byte[] toBytes() {

        return new Gson().toJson(this).getBytes(UTF8);
    }

    public boolean isFeedBack() {
        return feedBack;
    }

    public void setFeedBack(boolean feedBack) {
        this.feedBack = feedBack;
    }
}

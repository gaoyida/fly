package com.gaoyida.fly.common.protocol;

import com.gaoyida.fly.common.Constant;

/**
 * @author gaoyida
 * @date 2019/10/22 下午12:14
 */
public abstract class TLVMessage {

    private long msgId; // TODO

    private int errorCode = Constant.CODE_OK;

    private String errorMsg;

    private int length;

    public abstract MessageType getType();

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}

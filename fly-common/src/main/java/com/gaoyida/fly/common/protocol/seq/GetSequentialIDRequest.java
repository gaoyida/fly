package com.gaoyida.fly.common.protocol.seq;

import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.TLVUpStreamMessage;

/**
 * @author gaoyida
 * @date 2019/10/22 下午8:34
 */
public class GetSequentialIDRequest extends TLVUpStreamMessage {

    @Override
    public MessageType getType() {
        return MessageType.GET_SEQUENTIAL_ID_REQUEST;
    }
}

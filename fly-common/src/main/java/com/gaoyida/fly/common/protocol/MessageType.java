package com.gaoyida.fly.common.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoyida
 * @date 2019/10/22 下午12:18
 */
public enum MessageType {
    GET_SEQUENTIAL_ID_REQUEST((short) 10001),
    GET_SEQUENTIAL_ID_RESPONSE((short) 10002),


    GET_UNIQUE_ID_REQUEST((short) 10003),
    GET_UNIQUE_ID_RESPONSE((short) 10004),

    FORWARD_GET_ID_REQUEST((short) 10005),
    FORWARD_GET_ID_RESPONSE((short) 10006),

    GET_MASTER_RESPONSE((short) 10007);

    private static Map<Short, MessageType> valueMap;

    static {
        MessageType[] values = MessageType.values();
        valueMap = new HashMap<>();

        for (MessageType t : values) {
            valueMap.putIfAbsent(t.getType(), t);
        }
    }

    private short type;

    MessageType(short type) {
        this.type = type;
    }

    public static MessageType getMessageType(Short type) {
        return valueMap.get(type);
    }

    public short getType() {
        return type;
    }
}

package com.gaoyida.fly.gensrv.net.handler.biz;

import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.forward.GeneralForwardMasterRequest;
import com.gaoyida.fly.common.protocol.forward.GeneralForwardMasterResponse;
import com.gaoyida.fly.gensrv.net.handler.AbstractCallHandler;
import org.springframework.stereotype.Component;

/**
 * @author gaoyida
 * @date 2019/10/22 下午8:32
 */
@Component
public class ForwardResponseHandler extends AbstractCallHandler<GeneralForwardMasterResponse, GeneralForwardMasterRequest> {

    @Override
    public Class<GeneralForwardMasterResponse> getRequestMessageClass() {
        return GeneralForwardMasterResponse.class;
    }

    @Override
    public Class<GeneralForwardMasterRequest> getResponseMessageClass() {
        return GeneralForwardMasterRequest.class;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.FORWARD_GET_ID_RESPONSE;
    }
}

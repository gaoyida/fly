package com.gaoyida.fly.gensrv.net.handler.biz;

import com.gaoyida.fly.common.Constant;
import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.forward.GeneralForwardRequest;
import com.gaoyida.fly.common.protocol.forward.GeneralForwardResponse;
import com.gaoyida.fly.gensrv.core.generate.SequentialGeneratorService;
import com.gaoyida.fly.gensrv.core.generate.UniqueGeneratorService;
import com.gaoyida.fly.gensrv.net.handler.AbstractMessageHandler;
import org.jboss.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gaoyida
 * @date 2019/10/22 下午8:32
 */
@Component
public class ForwardRequestHandler extends AbstractMessageHandler<GeneralForwardRequest, GeneralForwardResponse> {

    @Resource
    private SequentialGeneratorService sequentialGeneratorService;

    @Resource
    private UniqueGeneratorService uniqueGeneratorService;

    @Override
    public void handleMessage(GeneralForwardRequest request, GeneralForwardResponse response, Channel channel) {

        if (MessageType.GET_SEQUENTIAL_ID_REQUEST.equals(request.getForwardType())) {
            response.setId(sequentialGeneratorService.getNextId());
        } else {
            response.setErrorCode(Constant.CODE_NOT_AVAILABLE);
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.FORWARD_GET_ID_REQUEST;
    }

    @Override
    public Class<GeneralForwardRequest> getRequestMessageClass() {
        return GeneralForwardRequest.class;
    }

    @Override
    public Class<GeneralForwardResponse> getResponseMessageClass() {
        return GeneralForwardResponse.class;
    }


}

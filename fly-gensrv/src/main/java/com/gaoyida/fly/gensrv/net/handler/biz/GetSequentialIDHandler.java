package com.gaoyida.fly.gensrv.net.handler.biz;

import com.gaoyida.fly.common.protocol.seq.GetSequentialIDRequest;
import com.gaoyida.fly.common.protocol.seq.GetSequentialIDResponse;
import com.gaoyida.fly.gensrv.core.generate.SequentialGeneratorService;
import com.gaoyida.fly.gensrv.net.handler.AbstractMessageHandler;
import org.jboss.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gaoyida
 * @date 2019/10/22 下午8:32
 */
@Component
public class GetSequentialIDHandler extends AbstractMessageHandler<GetSequentialIDRequest, GetSequentialIDResponse> {

    @Resource
    private SequentialGeneratorService sequentialGeneratorService;

    @Override
    public void handleMessage(GetSequentialIDRequest request, GetSequentialIDResponse response, Channel channel) {
        response.setId(sequentialGeneratorService.getNextId());
    }

    @Override
    public Class<GetSequentialIDRequest> getRequestMessageClass() {
        return GetSequentialIDRequest.class;
    }

    @Override
    public Class<GetSequentialIDResponse> getResponseMessageClass() {
        return GetSequentialIDResponse.class;
    }


}

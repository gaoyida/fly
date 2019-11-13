package com.gaoyida.fly.gensrv.net.handler.biz;

import com.gaoyida.fly.common.protocol.unqiue.GetUniqueIDRequest;
import com.gaoyida.fly.common.protocol.unqiue.GetUniqueIDResponse;
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
public class GetUniqueIDHandler extends AbstractMessageHandler<GetUniqueIDRequest, GetUniqueIDResponse> {

    @Resource
    private UniqueGeneratorService uniqueGeneratorService;

    @Override
    public void handleMessage(GetUniqueIDRequest request, GetUniqueIDResponse response, Channel channel) {
        response.setId(uniqueGeneratorService.getNextId());
    }

    @Override
    public Class<GetUniqueIDRequest> getRequestMessageClass() {
        return GetUniqueIDRequest.class;
    }

    @Override
    public Class<GetUniqueIDResponse> getResponseMessageClass() {
        return GetUniqueIDResponse.class;
    }


}

package com.gaoyida.fly.gensrv.net.handler.channel;

import com.gaoyida.fly.common.log.Logger;
import com.gaoyida.fly.common.log.LoggerFactory;
import com.gaoyida.fly.common.protocol.TLVUpStreamMessage;
import com.gaoyida.fly.gensrv.net.handler.biz.DispatcherHandler;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

/**
 * @author gaoyida
 * @date 2019/10/19 下午1:08
 */
public class TLVMessageDecoder extends LengthFieldBasedFrameDecoder {

    private static final int maxFrameLength = 1024;

    private static final int lengthFieldOffset = 2;

    private static final int lengthFieldLength = 4;

    private static Logger logger = LoggerFactory.getLogger(TLVMessageDecoder.class);

    private DispatcherHandler dispatcherHandler;

    public TLVMessageDecoder(DispatcherHandler dispatcherHandler) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.dispatcherHandler = dispatcherHandler;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer)
            throws Exception {
        ChannelBuffer frame;
        try {
            frame = (ChannelBuffer) super.decode(ctx, channel, buffer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            channel.close();
            return null;
        }

        if (frame == null) {
            return null;
        }

        short type = frame.getShort(0);
        int length = frame.getInt(2);

        long messageId = frame.getLong(6);

        byte[] dst = new byte[length - 8];
        frame.getBytes(14, dst, 0, length - 8);

        TLVUpStreamMessage request = dispatcherHandler.decode(type, dst);
        request.setLength(length);
        request.setMsgId(messageId);

        return request;

    }
}
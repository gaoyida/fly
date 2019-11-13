package com.gaoyida.fly.gensrv.net.handler.channel;

import com.gaoyida.fly.common.protocol.TLVDownStreamMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import static org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer;

/**
 * @author gaoyida
 * @date 2019/10/23 下午5:20
 */
public class TLVMessageEncoder extends OneToOneEncoder {

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {

        TLVDownStreamMessage outMessage = (TLVDownStreamMessage) (msg);

        byte[] bi = outMessage.toBytes();
        outMessage.setLength(bi.length + 8);

        ChannelBuffer channelBuffer = dynamicBuffer(bi.length + 14, ctx.getChannel().getConfig().getBufferFactory());

        ChannelBufferOutputStream bout = new ChannelBufferOutputStream(channelBuffer);

        bout.writeShort(outMessage.getType().getType());
        bout.writeInt(bi.length + 8);
        bout.writeLong(outMessage.getMsgId());
        bout.write(bi);
        ChannelBuffer encoded = bout.buffer();
        bout.close();
        return encoded;

    }


}

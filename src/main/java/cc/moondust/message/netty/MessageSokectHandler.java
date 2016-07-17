package cc.moondust.message.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import org.springframework.stereotype.Controller;

/**
 * Created by Tristan on 16/7/17.
 */
@Controller
public class MessageSokectHandler extends SimpleChannelInboundHandler<Object>{


    private WebSocketClientHandshaker handshaker;
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

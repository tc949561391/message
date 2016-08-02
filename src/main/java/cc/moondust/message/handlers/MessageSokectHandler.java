package cc.moondust.message.handlers;

import cc.moondust.message.repository.ChannelRepository;
import cc.moondust.message.utils.JSONUtil;
import cc.moondust.message.utils.ParamsUtil;
import cc.moondust.message.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.rtsp.RtspHeaderNames.HOST;

/**
 * Created by Tristan on 16/7/17.
 */
@Component
@ChannelHandler.Sharable
public class MessageSokectHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(MessageSokectHandler.class.getName());

    private String websocketPath="";

    private WebSocketServerHandshaker handshaker;

    @Autowired
    ChannelRepository channelRepository;



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除
        channelRepository.removeChannelObj(ctx.channel());
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx,
                                       WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame
                    .retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();

        // 群发
        try {
            Map<String, Object> map = JSONUtil.jsonToMap(JSON.parseObject(request));
            String from=map.get("from").toString();
            String to = map.get("to").toString();
            String message = map.get("message").toString();
            HashMap<String, String> res = new HashMap<>();
            res.put("message",message);
            res.put("from",from);
            String tomessage = JSONUtil.objectToJsonStr(res);
            TextWebSocketFrame tws = new TextWebSocketFrame(tomessage);
            channelRepository.getChannel(to).writeAndFlush(tws);
        } catch (Exception e) {

        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            //说明不是upgrade的请求头，也就不是websocket连接请求
            return;
        }
        HashMap<String, String> params = ParamsUtil.buidParams(req);
        String access_token = params.get("access_token");
        if (StringUtil.empty(access_token)) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                this.getWebSocketLocation(req), null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            //这里是连接握手协议成功了
            NioSocketChannel localChannel = (NioSocketChannel) ctx.channel();
            handshaker.handshake(localChannel, req);
            if (localChannel.isActive()) {
                // 添加
                channelRepository.saveChannel(access_token, localChannel);
            }
        }
    }


    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static boolean isKeepAlive(FullHttpRequest req) {
        return false;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private String getWebSocketLocation(HttpRequest req) {
        return "ws://" + req.headers().get(HOST) + websocketPath;
    }

}

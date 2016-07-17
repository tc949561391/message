package cc.moondust.message.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Tristan on 16/7/17.
 */
@Component
public class WebSocketInitializer extends ChannelInitializer<SocketChannel>{

    @Autowired
    MessageSokectHandler  messageSokectHandler;



    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("http-codec",new HttpServerCodec());
        pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
        pipeline.addLast("http-chunked",new ChunkedWriteHandler());
        pipeline.addLast("handler",messageSokectHandler);
    }
}

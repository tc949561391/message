package cc.moondust.message.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Tristan on 16/7/17.
 */
@Component
public class MessageSocketServer {

    private static Logger log = LoggerFactory.getLogger(MessageSocketServer.class);
    @Autowired
    private WebSocketInitializer webSocketInitializer;

    @Value("#{cnf.messageserver_port}")
    private int port;

    public void run() {

        log.info("the server is bootting................");
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(webSocketInitializer);
            Channel channel = serverBootstrap.bind(port).sync().channel();
            log.info("the server is boot at port="+port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }


}

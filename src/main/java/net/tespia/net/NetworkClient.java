package net.tespia.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NetworkClient extends Thread {

    private static final Logger log = LoggerFactory.getLogger(NetworkClient.class);
    public static final int RETRY_TIME = 5;

    private String host;
    private int port;
    private EventLoopGroup workerGroup;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        connectAndLogException();
    }

    protected void onConnectFailure(Exception e) {
    }

    public void connectToRemoteServer() throws InterruptedException {
        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(getPipeline());
                }
            });

            ChannelFuture future = b
                    .connect(host, port)
                    .addListener((e) -> {
                        if (!e.isSuccess()) {
                            log.info("Reconnecting to server {}...", getServerName());
                            onConnectFailure(null);
                        }
                    }).sync();

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void shutdown() {
        if (workerGroup != null && !workerGroup.isShutdown()) {
            workerGroup.shutdownGracefully();
        }
    }

    private void connectAndLogException() {
        try {
            connectToRemoteServer();
        } catch (InterruptedException e1) {
            log.debug("Error reconnecting to {}", getServerName(), e1);
            onConnectFailure(e1);
        }
    }

    protected ChannelHandler[] getPipeline() {
        return new ChannelHandler[]{
                new InPacketDecoder(),
                getInBoundHandler(),
                new OutPacketEncoder(),
        };
    }

    protected String getServerName() {
        return this.getClass().getSimpleName();
    }

    protected abstract void onConnectionClosed(ChannelHandlerContext ctx);

    protected abstract void onConnectionOpened(ChannelHandlerContext ctx);


    protected abstract ChannelInboundHandler getInBoundHandler();

}

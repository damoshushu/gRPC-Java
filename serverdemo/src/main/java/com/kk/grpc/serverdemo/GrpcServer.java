package com.kk.grpc.serverdemo;

import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import io.grpc.Server;

import java.io.IOException;

@Component
@Slf4j
public class GrpcServer {
    private static final int DEFAULT_PORT = 8088;
    private Server server;
    private void start() throws IOException {
        server.start();
        log.info("Server has started, listening on " + DEFAULT_PORT);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> GrpcServer.this.stop()));
    }

    private void stop() {
        if (server != null)
            server.shutdown();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    @PostConstruct
    public void run() {
        new Thread(()->{
            server = ServerBuilder.forPort(DEFAULT_PORT).addService(new TTSServiceImpl()).build();
            try {
                start();
                blockUntilShutdown();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }


}

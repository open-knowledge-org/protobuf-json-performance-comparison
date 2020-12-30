package com.mioto.wss.client.txt;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

public class TextClientApp {

    final static CountDownLatch messageLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = "ws://localhost:4443/json";
            System.out.println("Connecting to " + uri);
            container.connectToServer(WebsocketClientTextEndpoint.class, URI.create(uri));
            messageLatch.await(100, TimeUnit.SECONDS);
        } catch (DeploymentException | InterruptedException | IOException ex) {
            ex.printStackTrace();
        }
    }
}

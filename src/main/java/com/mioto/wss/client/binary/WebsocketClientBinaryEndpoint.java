package com.mioto.wss.client.binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import example.simple.Simple.SimpleMessage;

@ClientEndpoint
public class WebsocketClientBinaryEndpoint {
    @OnOpen
    public void onOpen(Session session) {
        
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
        try {
            System.out.println("Sending binary message to endpoint");
            
            Date before = new Date();

            for (int i = 0; i < 100000; i++) {
                SimpleMessage.Builder builder = SimpleMessage.newBuilder();
                builder.setId(42)  // set the id field
                .setIsSimple(true)  // set the is_simple field
                .setName("My Simple Message Name"); // set the name field
                
                builder.addSampleList(1)
                .addSampleList(2)
                .addSampleList(3)
                .addAllSampleList(Arrays.asList(4, 5, 6));
                
                SimpleMessage message = builder.build();
                
                final ByteBuffer buffer = ByteBuffer.wrap(message.toByteArray());
                session.getBasicRemote().sendBinary( buffer);
            }
            long timeElapsed = (new Date().getTime() - before.getTime()) / 1000;
            System.out.println(timeElapsed + " seconds | ProtoBuff");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void processMessage(String message) {
        System.out.println("Received message in client: " + message);
        BinaryClientApp.messageLatch.countDown();
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
}
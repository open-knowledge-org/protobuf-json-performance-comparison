package com.mioto.wss.client.txt;

import java.io.IOException;
import java.util.Date;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.ObjectMapper;

@ClientEndpoint
public class WebsocketClientTextEndpoint {
    @OnOpen
    public void onOpen(Session session) {
        
        
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
        try {
            System.out.println("Sending text message to endpoint");
            
            Date before = new Date();
            for (int i = 0; i < 100000; i++) {
                String json = "{\r\n" + 
                        "    \"id\": 42,\r\n" + 
                        "    \"simple\": true,\r\n" + 
                        "    \"name\": \"My Simple Message Name \",\r\n" + 
                        "    \"sampleList\": [1, 2, 3, 4, 5, 6]\r\n" + 
                        "}";

                ObjectMapper mapper = new ObjectMapper();
                SimpleJson simpleJson = mapper.readValue(json, SimpleJson.class);
                session.getBasicRemote().sendText(mapper.writeValueAsString(simpleJson));
                
            }
            long timeElapsed = (new Date().getTime() - before.getTime()) / 1000;
            System.out.println(timeElapsed + " seconds | JSON ");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void processMessage(String message) {
        System.out.println("Received message in client: " + message);
        TextClientApp.messageLatch.countDown();
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
}
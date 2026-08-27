package com.palette.task;

import com.palette.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WebSocketTask {
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * Send a message to the client via WebSocket every 5 seconds
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageToClient() {
        // webSocketServer.sendToAllClient("This is a message from the server: " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    }
}

package com.palette.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket service
 */
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    //Store session objects
    private static Map<String, Session> sessionMap = new HashMap();

    /**
     * Method called when the connection is successfully established
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("Client: " + sid + " connected");
        sessionMap.put(sid, session);
    }

    /**
     * Method called after receiving a message from the client
     *
     * @param message the message sent from the client
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("Received message from client: " + sid + ": " + message);
    }

    /**
     * Method called when the connection is closed
     *
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("Connection closed: " + sid);
        sessionMap.remove(sid);
    }

    /**
     * Broadcast to all clients
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //Server sends message to client
                session.getBasicRemote().sendText(message);
                System.out.println("Sending websocket message");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

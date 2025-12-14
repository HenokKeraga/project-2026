package org.example.websocketdemo.service;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    public WebSocketHandler() {
        super();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        sessions.add(session);

        session.sendMessage(new TextMessage(session.getId() + " connected"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 1. Log the incoming TEXT message (Fixes the no-print issue)
        System.out.println("Received message: " + message.getPayload());

        // 2. Broadcast the response message to all sessions except the sender
        for (WebSocketSession s : sessions) {
            try {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    // Sending a TextMessage as a response
                    s.sendMessage(new TextMessage(message.getPayload()));
                }
            } catch (Exception e) {
                // It's generally good practice to log or handle exceptions
                System.err.println("Failed to send message to session " + s.getId() + ": " + e.getMessage());
            }
        }
    }

//    @Override
//    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
//        System.out.println(message.getPayload());
//
//        for (WebSocketSession s : sessions) {
//            try {
//                if (s.isOpen()) {
//                    s.sendMessage(new TextMessage(" this is websocket demo "));
//                }
//
//            } catch (Exception e) {
//
//            }
//        }
//
//    }
}

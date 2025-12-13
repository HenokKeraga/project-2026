package org.example.doc.service;


import org.example.doc.dto.ClientOpMessage;
import org.example.doc.dto.ServerOpMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DocWebSocketHandler extends TextWebSocketHandler {

    private final DocService docService;
    private final ObjectMapper mapper = new ObjectMapper();

    // docId -> sessions
    private final Map<UUID, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    public DocWebSocketHandler(DocService docService) {
        this.docService = docService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UUID docId = docIdFrom(session);
        rooms.computeIfAbsent(docId, k -> ConcurrentHashMap.newKeySet()).add(session);

        // Send initial state (optional)
        // (Postman can also call GET /api/docs/{docId})
        session.sendMessage(new TextMessage("{\"type\":\"CONNECTED\",\"docId\":\"" + docId + "\"}"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UUID docId = docIdFrom(session);

        ClientOpMessage clientMsg = mapper.readValue(message.getPayload(), ClientOpMessage.class);

        ServerOpMessage serverMsg = docService.applyOp(docId, clientMsg);

        // broadcast to all sessions in room
        broadcast(docId, mapper.writeValueAsString(serverMsg));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            UUID docId = docIdFrom(session);
            Set<WebSocketSession> set = rooms.get(docId);
            if (set != null) {
                set.remove(session);
                if (set.isEmpty()) rooms.remove(docId);
            }
        } catch (Exception ignored) {}
    }

    private void broadcast(UUID docId, String json) {
        Set<WebSocketSession> set = rooms.getOrDefault(docId, Set.of());
        for (WebSocketSession s : set) {
            try {
                if (s.isOpen()) s.sendMessage(new TextMessage(json));
            } catch (Exception ignored) {}
        }
    }

    private UUID docIdFrom(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) throw new IllegalArgumentException("Missing URI");
        // /ws/docs/{docId}
        String[] parts = uri.getPath().split("/");
        String last = parts[parts.length - 1];
        return UUID.fromString(last);
    }
}


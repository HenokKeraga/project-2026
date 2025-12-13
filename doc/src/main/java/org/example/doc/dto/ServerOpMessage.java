package org.example.doc.dto;


import java.util.UUID;

public record ServerOpMessage(
        UUID docId,
        long serverVersion,
        String userId,
        String clientOpId,
        OpPayload op,
        String error // null if ok
) {}

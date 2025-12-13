package org.example.doc.dto;


import java.util.UUID;

public record DocResponse(UUID docId, String title, long version, String content) {}


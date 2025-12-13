package org.example.doc.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OpPayload(
        @NotBlank String type, // INSERT, DELETE
        @Min(0) int pos,
        String text,
        @Min(0) int length
) {}


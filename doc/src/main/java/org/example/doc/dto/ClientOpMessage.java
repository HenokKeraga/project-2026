package org.example.doc.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientOpMessage(
        @NotBlank String clientOpId,
        long baseVersion,
        @NotBlank String userId,
        @NotNull @Valid OpPayload op
) {}


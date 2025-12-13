package org.example.doc.dto;


import jakarta.validation.constraints.NotBlank;

public record CreateDocRequest(@NotBlank String title) {}


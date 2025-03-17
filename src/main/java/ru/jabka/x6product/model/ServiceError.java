package ru.jabka.x6product.model;

import lombok.Builder;

@Builder
public record ServiceError(Boolean success, String message) {
}
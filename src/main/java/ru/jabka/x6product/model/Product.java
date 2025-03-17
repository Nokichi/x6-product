package ru.jabka.x6product.model;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record Product(
        Long id,
        String name,
        Double price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime usedAt
) implements Serializable {
}
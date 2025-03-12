package ru.jabka.x6product.model;

import java.io.Serializable;
import java.util.Map;

public record ProductExists(
        Map<Long, Boolean> productChecklist
) implements Serializable {
}
package ru.jabka.x6product.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.jabka.x6product.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class ProductMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Product.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .price(rs.getDouble("price"))
                .createdAt(rs.getObject("created_at", Timestamp.class).toLocalDateTime())
                .updatedAt(rs.getObject("updated_at", Timestamp.class).toLocalDateTime())
                .usedAt(rs.getObject("used_at", Timestamp.class).toLocalDateTime())
                .build();
    }
}
package ru.jabka.x6product.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jabka.x6product.exception.BadRequestException;
import ru.jabka.x6product.model.Product;
import ru.jabka.x6product.repository.mapper.ProductMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProductMapper productMapper;

    private static final String INSERT = """
            INSERT INTO x6.product (name, price)
            VALUES (:name, :price)
            RETURNING *
            """;

    private static final String UPDATE = """
            UPDATE x6.product
            SET name = :name, price = :price, updated_at = CURRENT_TIMESTAMP
            WHERE id = :id
            RETURNING *
            """;

    private static final String SET_USAGES = """
            UPDATE x6.product
            SET used_at = CURRENT_TIMESTAMP
            WHERE id = :id
            RETURNING *
            """;

    private static final String GET_BY_ID = """
            SELECT * 
            FROM x6.product
            WHERE id = :id
            """;

    private static final String EXISTS = """
            SELECT
                ids.id,
                EXISTS (
                    SELECT 1
                    FROM x6.product
                    WHERE x6.product.id = ids.id
                ) AS existence
            FROM (VALUES ? ) AS ids(id)
            """;

    public Product insert(Product product) {
        return jdbcTemplate.queryForObject(INSERT, productToSql(product), productMapper);
    }

    public Product update(Product product) {
        return jdbcTemplate.queryForObject(UPDATE, productToSql(product), productMapper);
    }

    public Product getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), productMapper);
        } catch (Throwable e) {
            throw new BadRequestException(String.format("Товар с id %d не найден", id));
        }
    }

    public Map<Long, Boolean> isExists(Set<Long> ids) {
        String values = ids.stream()
                .map(x -> "(" + x + ")")
                .collect(Collectors.joining(","));
        String sqlBuilder = EXISTS.replace("?", values);
        return jdbcTemplate.query(sqlBuilder, rs -> {
            Map<Long, Boolean> result = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                boolean exists = rs.getBoolean("existence");
                result.put(id, exists);
            }
            return result;
        });
    }

    public Product setUsages(Long id) {
        return jdbcTemplate.queryForObject(SET_USAGES, new MapSqlParameterSource("id", id), productMapper);
    }

    private MapSqlParameterSource productToSql(Product product) {
        return new MapSqlParameterSource()
                .addValue("id", product.id())
                .addValue("name", product.name())
                .addValue("price", product.price())
                .addValue("created_at", product.createdAt())
                .addValue("updated_at", product.updatedAt())
                .addValue("used_at", product.usedAt());
    }
}
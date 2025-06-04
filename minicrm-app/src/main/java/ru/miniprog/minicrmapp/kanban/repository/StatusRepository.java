package ru.miniprog.minicrmapp.kanban.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrmapp.kanban.model.Status;

@Repository
public class StatusRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Status> statusRowMapper = (rs, rowNum) -> {
        Status status = new Status();
        status.setId(rs.getLong("id"));
        status.setName(rs.getString("name"));
        return status;
    };

    public StatusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Status> findAll() {
        String sql = "SELECT * FROM status";
        return jdbcTemplate.query(sql, statusRowMapper);
    }

    public Status save(Status status) {
        if (status.getId() == null) {
            String sql = "INSERT INTO status (name) VALUES (?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class, status.getName());
            status.setId(id);
        } else {
            String sql = "UPDATE status SET name = ? WHERE id = ?";
            jdbcTemplate.update(sql, status.getName(), status.getId());
        }
        return status;
    }

    public Optional<Status> findById(Long id) {
        String sql = "SELECT * FROM status WHERE id = ?";
        List<Status> statuses = jdbcTemplate.query(sql, statusRowMapper, id);
        return statuses.isEmpty() ? Optional.empty() : Optional.of(statuses.get(0));
    }

    public void deleteById(Long id) {
        String sqlTask = "DELETE FROM task WHERE status_id = ?";
        jdbcTemplate.update(sqlTask, id);

        String sqlStatus = "DELETE FROM status WHERE id = ?";
        jdbcTemplate.update(sqlStatus, id);
    }
}

package ru.miniprog.minicrmapp.kanban.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.miniprog.minicrmapp.chat.internal.model.UserCrm;
import ru.miniprog.minicrmapp.kanban.model.Task;

@Repository
public class TaskRepository {
    private final JdbcTemplate jdbcTemplate;
    private final StatusRepository statusRepository;
    private final RowMapper<Task> taskRowMapper = (rs, rowNum) -> {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setCreatedAt(rs.getTimestamp("created_at"));
        task.setProfit(rs.getDouble("profit"));
        task.setOwnerId(rs.getLong("owner_id"));
        return task;
    };

    public TaskRepository(JdbcTemplate jdbcTemplate, StatusRepository statusRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.statusRepository = statusRepository;
    }

    public List<Task> findAll() {
        String sql = "SELECT * FROM task";
        List<Task> tasks = jdbcTemplate.query(sql, taskRowMapper);
        tasks.forEach(this::loadStatus);
        return tasks;
    }

    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM task WHERE id = ?";
        List<Task> tasks = jdbcTemplate.query(sql, taskRowMapper, id);
        tasks.forEach(this::loadStatus);
        return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.get(0));
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            String sql = "INSERT INTO task (title, description, created_at, profit, owner_id, status_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    task.getTitle(),
                    task.getDescription(),
                    task.getCreatedAt(),
                    task.getProfit(),
                    task.getOwnerId(),
                    task.getStatus().getId());
            task.setId(id);
        } else {
            String sql = "UPDATE task SET title = ?, description = ?, created_at = ?, profit = ?, owner_id = ?, status_id = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    task.getTitle(),
                    task.getDescription(),
                    task.getCreatedAt(),
                    task.getProfit(),
                    task.getOwnerId(),
                    task.getStatus().getId(),
                    task.getId());
        }
        return task;
    }

    private void loadStatus(Task task) {
        String sql = "SELECT status_id FROM task WHERE id = ?";
        Long statusId = jdbcTemplate.queryForObject(sql, Long.class, task.getId());
        task.setStatus(statusRepository.findById(statusId).orElse(null));
    }
}

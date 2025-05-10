package ru.miniprog.minicrmapp.kanban.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrmapp.chat.internal.model.ChatRoom;
import ru.miniprog.minicrmapp.kanban.mapper.NoteMapper;
import ru.miniprog.minicrmapp.kanban.model.Note;

import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoteRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NoteMapper noteMapper;

    public List<Note> findByTaskId(long taskId) {
        String sql = "SELECT * FROM note WHERE task_id = ?";
        return jdbcTemplate.query(sql, noteMapper, taskId);
    }

    public Note save(Note note) {
        if (note.getId() == null) {
            String sql = "INSERT INTO note (message, task_id, date) VALUES (?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    note.getMessage(),
                    note.getTaskId(),
                    note.getDate());
            note.setId(id);
        } else {
            String sql = "UPDATE note SET message = ?, task_id = ?, date = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    note.getMessage(),
                    note.getTaskId(),
                    note.getDate(),
                    note.getId());
        }
        return note;
    }
}

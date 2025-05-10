package ru.miniprog.minicrmapp.kanban.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.miniprog.minicrmapp.kanban.model.Note;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NoteMapper implements RowMapper<Note> {
    @Override
    public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
        Note note = new Note();
        note.setId(rs.getLong("id"));
        note.setMessage(rs.getString("message"));
        note.setTaskId(rs.getLong("task_id"));
        return note;
    }
}

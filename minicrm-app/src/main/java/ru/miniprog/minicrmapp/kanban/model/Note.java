package ru.miniprog.minicrmapp.kanban.model;

import java.util.Date;

public class Note {
    private Long id;
    private String message;
    private long taskId;
    private Date date = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Note(Long id, String message, long taskId, Date date) {
        this.id = id;
        this.message = message;
        this.taskId = taskId;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Note() {
    }

}

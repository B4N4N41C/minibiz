package ru.miniprog.minicrmapp.kanban.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private Long id;
    private String message;
    private long taskId;
    private Date date = new Date();
}

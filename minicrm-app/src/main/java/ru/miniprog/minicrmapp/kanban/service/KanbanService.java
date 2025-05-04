package ru.miniprog.minicrmapp.kanban.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.miniprog.minicrmapp.kanban.api.payload.UpdateTaskStatusPayload;
import ru.miniprog.minicrmapp.kanban.model.Task;
import ru.miniprog.minicrmapp.kanban.repository.StatusRepository;
import ru.miniprog.minicrmapp.kanban.repository.TaskRepository;

@RequiredArgsConstructor
@Service
public class KanbanService {

    private final StatusRepository statusRepository;

    private final TaskRepository taskRepository;

    public void updateTaskStatus(UpdateTaskStatusPayload payload) {
        Task task = this.taskRepository.findById(payload.task_id()).get();
        task.setStatus(this.statusRepository.findById(payload.status_id()).get());
        this.taskRepository.save(task);
    }
}

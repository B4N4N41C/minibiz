package ru.miniprog.minicrm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.miniprog.minicrm.api.payload.UpdateTaskStatusPayload;
import ru.miniprog.minicrm.model.Task;
import ru.miniprog.minicrm.repository.StatusRepository;
import ru.miniprog.minicrm.repository.TaskRepository;

@RequiredArgsConstructor
@Service
public class KanbanService {

    private final StatusRepository statusRepository;

    private final TaskRepository taskRepository;

    @Transactional
    public void updateTaskStatus(UpdateTaskStatusPayload payload) {
        Task task = this.taskRepository.getReferenceById(payload.task_id());
        task.setStatus(this.statusRepository.getReferenceById(payload.status_id()));
        this.taskRepository.save(task);
    }
}

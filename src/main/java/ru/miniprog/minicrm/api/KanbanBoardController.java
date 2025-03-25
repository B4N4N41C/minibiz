package ru.miniprog.minicrm.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.miniprog.minicrm.api.payload.NewStatusPayload;
import ru.miniprog.minicrm.api.payload.NewTaskPayload;
import ru.miniprog.minicrm.api.payload.UpdateTaskStatusPayload;
import ru.miniprog.minicrm.model.Status;
import ru.miniprog.minicrm.model.Task;
import ru.miniprog.minicrm.repository.StatusRepository;
import ru.miniprog.minicrm.repository.TaskRepository;
import ru.miniprog.minicrm.service.KanbanService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kanban")
public class KanbanBoardController {

    private final StatusRepository statusRepository;

    private final KanbanService kanbanService;
    private final TaskRepository taskRepository;

    @Operation(summary = "Получение статусов")
    @GetMapping("/status")
    public List<Status> getAllStatus(){
        return statusRepository.findAll();
    }

    @Operation(summary = "Добавление статусов")
    @PostMapping("/status")
    public Status addStatus(@RequestBody NewStatusPayload status){
        Status newStatus = new Status();
        newStatus.setName(status.name());
        return statusRepository.save(newStatus);
    }

    @Operation(summary = "Получение статусов")
    @GetMapping("/task")
    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }

    @Operation(summary = "Добавление задачи")
    @PostMapping("/task")
    public Task addTask(@RequestBody NewTaskPayload payload){
        Task task = new Task();
        task.setTitle(payload.title());
        task.setDescription(payload.description());
        task.setStatus(statusRepository.getReferenceById(payload.status_id()));
        return taskRepository.save(task);
    }

    @Operation(summary = "Обнавление статуса задачи")
    @PatchMapping("/task/new-status")
    public UpdateTaskStatusPayload updateStatusTask(@RequestBody UpdateTaskStatusPayload payload) {
        this.kanbanService.updateTaskStatus(payload);
        return payload;
    }

}

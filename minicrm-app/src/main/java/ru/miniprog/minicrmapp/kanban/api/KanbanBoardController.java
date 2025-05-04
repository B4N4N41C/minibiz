package ru.miniprog.minicrmapp.kanban.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.miniprog.minicrmapp.kanban.api.payload.NewStatusPayload;
import ru.miniprog.minicrmapp.kanban.api.payload.NewTaskPayload;
import ru.miniprog.minicrmapp.kanban.api.payload.UpdateTaskPayload;
import ru.miniprog.minicrmapp.kanban.api.payload.UpdateTaskStatusPayload;
import ru.miniprog.minicrmapp.kanban.model.Status;
import ru.miniprog.minicrmapp.kanban.model.Task;
import ru.miniprog.minicrmapp.kanban.repository.StatusRepository;
import ru.miniprog.minicrmapp.kanban.repository.TaskRepository;
import ru.miniprog.minicrmapp.kanban.service.KanbanService;

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

    @Operation(summary = "Редактирование статуса")
    @PatchMapping("/status/{id}")
    public Status updateStatus(@PathVariable Long id, @RequestBody NewStatusPayload status){
        Status newStatus = statusRepository.findById(id).get();
        newStatus.setName(status.name());
        return statusRepository.save(newStatus);
    }

    @Operation(summary = "Удаление статусов")
    @DeleteMapping("/status/{id}")
    public void deleteStatus(@PathVariable Long id){
        statusRepository.deleteById(id);
    }

    @Operation(summary = "Редактирование сделок")
    @PatchMapping("/task/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody UpdateTaskPayload taskPayload){
        Task task = taskRepository.findById(id).get();
        task.setTitle(taskPayload.title());
        task.setDescription(taskPayload.description());
        task.setProfit(taskPayload.profit());
        return taskRepository.save(task);
    }

    @Operation(summary = "Получение сделок")
    @GetMapping("/task")
    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }

    @Operation(summary = "Добавление сделок")
    @PostMapping("/task")
    public Task addTask(@RequestBody NewTaskPayload payload){
        Task task = new Task();
        task.setTitle(payload.title());
        task.setDescription(payload.description());
        task.setStatus(statusRepository.findById(payload.status_id()).get());
        task.setOwnerId(payload.owner());
        return taskRepository.save(task);
    }

    @Operation(summary = "Обнавление статуса задачи")
    @PatchMapping("/task/new-status")
    public UpdateTaskStatusPayload updateStatusTask(@RequestBody UpdateTaskStatusPayload payload) {
        this.kanbanService.updateTaskStatus(payload);
        return payload;
    }

}

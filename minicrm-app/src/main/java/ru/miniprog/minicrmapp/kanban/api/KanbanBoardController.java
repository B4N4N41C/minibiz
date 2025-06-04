package ru.miniprog.minicrmapp.kanban.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.miniprog.minicrmapp.chat.api.ChatController;
import ru.miniprog.minicrmapp.kanban.api.payload.*;
import ru.miniprog.minicrmapp.kanban.model.Note;
import ru.miniprog.minicrmapp.kanban.model.Status;
import ru.miniprog.minicrmapp.kanban.model.Task;
import ru.miniprog.minicrmapp.kanban.repository.NoteRepository;
import ru.miniprog.minicrmapp.kanban.repository.StatusRepository;
import ru.miniprog.minicrmapp.kanban.repository.TaskRepository;
import ru.miniprog.minicrmapp.kanban.service.KanbanService;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kanban")
public class KanbanBoardController {

    private final StatusRepository statusRepository;
    private final KanbanService kanbanService;
    private final TaskRepository taskRepository;
    private final NoteRepository noteRepository;

    Logger log = LoggerFactory.getLogger(KanbanBoardController.class);

    @Operation(summary = "Получить список всех статусов")
    @GetMapping("/status")
    public List<Status> getAllStatus() {
        log.info("Запрос на получение всех статусов");
        List<Status> statuses = statusRepository.findAll();
        log.info("Найдено {} статусов", statuses.size());
        return statuses;
    }

    @Operation(summary = "Добавить новый статус")
    @PostMapping("/status")
    public Status addStatus(@RequestBody NewStatusPayload status) {
        log.info("Создание нового статуса: {}", status.name());
        Status newStatus = new Status();
        newStatus.setName(status.name());
        Status savedStatus = statusRepository.save(newStatus);
        log.info("Статус успешно создан с ID: {}", savedStatus.getId());
        return savedStatus;
    }

    @Operation(summary = "Обновить существующий статус по ID")
    @PatchMapping("/status/{id}")
    public Status updateStatus(@PathVariable Long id, @RequestBody NewStatusPayload status) {
        Status newStatus = statusRepository.findById(id).get();
        newStatus.setName(status.name());
        return statusRepository.save(newStatus);
    }

    @Operation(summary = "Удалить статус по ID")
    @DeleteMapping("/status/{id}")
    public void deleteStatus(@PathVariable Long id) {
        statusRepository.deleteById(id);
    }

    @Operation(summary = "Обновить существующую задачу по ID")
    @PatchMapping("/task/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody UpdateTaskPayload taskPayload) {
        Task task = taskRepository.findById(id).get();
        task.setTitle(taskPayload.title());
        task.setDescription(taskPayload.description());
        task.setProfit(taskPayload.profit());
        return taskRepository.save(task);
    }

    @Operation(summary = "Получить список всех задач")
    @GetMapping("/task")
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    @Operation(summary = "Добавить новую задачу")
    @PostMapping("/task")
    public Task addTask(@RequestBody NewTaskPayload payload) {
        log.info("Создание новой задачи: {}", payload.title());
        Task task = new Task();
        task.setTitle(payload.title());
        task.setDescription(payload.description());
        task.setStatus(statusRepository.findById(payload.status_id()).get());
        task.setOwnerId(payload.owner());
        Task savedTask = taskRepository.save(task);
        log.info("Задача успешно создана с ID: {}", savedTask.getId());
        return savedTask;
    }

    @Operation(summary = "Обновить статус задачи")
    @PatchMapping("/task/new-status")
    public UpdateTaskStatusPayload updateStatusTask(@RequestBody UpdateTaskStatusPayload payload) {
        this.kanbanService.updateTaskStatus(payload);
        return payload;
    }

    @Operation(summary = "Получить список всех заметок по ID задачи")
    @GetMapping("/notes/{id}")
    public List<Note> getAllNotes(@PathVariable long id) {
        return noteRepository.findByTaskId(id);
    }

    @Operation(summary = "Добавить новую заметку")
    @PostMapping("/notes")
    public Note addNotes(@RequestBody NewNote note) {
        return noteRepository.save(new Note(null, note.message(), note.task_id(), new Date()));
    }

    @Operation(summary = "Удалить задачу по ID")
    @DeleteMapping("/task/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}

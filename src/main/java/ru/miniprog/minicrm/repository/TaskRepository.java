package ru.miniprog.minicrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.miniprog.minicrm.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

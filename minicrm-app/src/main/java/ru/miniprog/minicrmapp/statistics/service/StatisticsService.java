package ru.miniprog.minicrmapp.statistics.service;

import org.springframework.stereotype.Service;
import ru.miniprog.minicrmapp.kanban.model.Task;
import ru.miniprog.minicrmapp.statistics.model.StatisticsResponse;
import ru.miniprog.minicrmapp.statistics.model.UserStatistics;
import ru.miniprog.minicrmapp.users.repository.UserRepository;
import ru.miniprog.minicrmapp.chat.repository.MessageRepository;
import ru.miniprog.minicrmapp.kanban.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final TaskRepository taskRepository;

    public StatisticsResponse getDashboardStatistics() {
        var users = userRepository.findAll();
        var messages = messageRepository.findAll();
        var tasks = taskRepository.findAll();

        var userStatistics = users.stream()
                .map(user -> {
                    var userMessages = messages.stream()
                            .filter(msg -> msg.getSenderName().equalsIgnoreCase(user.getUsername()))
                            .count();

                    var userTasks = tasks.stream()
                            .filter(task -> task.getOwnerId().equals(user.getId()))
                            .count();

                    var userProfit = tasks.stream()
                            .filter(task -> task.getOwnerId().equals(user.getId()) &&
                                    task.getStatus().getId().equals(getLastStatusId(tasks)))
                            .mapToDouble(task -> task.getProfit() != null ? task.getProfit() : 0)
                            .sum();

                    return new UserStatistics(
                            user.getUsername(),
                            userMessages,
                            userTasks,
                            userProfit
                    );
                })
                .filter(stat -> stat.messages() > 0 || stat.tasks() > 0 || stat.profit() > 0)
                .toList();

        return new StatisticsResponse(userStatistics);
    }

    private Long getLastStatusId(List<Task> tasks) {
        return tasks.stream()
                .map(task -> task.getStatus().getId())
                .max(Long::compareTo)
                .orElse(null);
    }
}

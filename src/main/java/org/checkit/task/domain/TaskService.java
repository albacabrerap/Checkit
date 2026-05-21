package org.checkit.task.domain;

import lombok.RequiredArgsConstructor;
import org.checkit.exception.BusinessException;
import org.checkit.task.dto.CreateTaskDto;
import org.checkit.task.events.TaskCompletedEvent;
import org.checkit.task.infrastructure.TaskRepository;
import org.checkit.user.domain.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;

    @Transactional
    public Task createTask(User user, CreateTaskDto dto) {
        Task task = modelMapper.map(dto, Task.class);
        task.setUser(user);
        task.setState(State.PENDIENTE);

        Task savedTask = (Task) taskRepository.save(task);
        eventPublisher.publishEvent(new TaskCreatedEvent(savedTask, user.getEmail()));

        return savedTask;
    }

    @Transactional
    public Task completeTask(Long taskId, User user) throws Throwable {
        Task task = (Task) taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Unauthorized task modification attempt");
        }

        task.setState(State.COMPLETADA);
        Task updatedTask = (Task) taskRepository.save(task);

        eventPublisher.publishEvent(new TaskCompletedEvent(updatedTask));

        return updatedTask;
    }

    public Task getRandomTaskFromWheel(User user) {
        List<Task> pendingTasks = taskRepository.findByUserAndState(user, State.PENDIENTE);
        if (pendingTasks.isEmpty()) {
            throw new BusinessException("No pending tasks available for the lucky wheel!");
        }
        int randomIndex = (int) (Math.random() * pendingTasks.size());
        return pendingTasks.get(randomIndex);
    }
}
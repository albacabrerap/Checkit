package org.checkit.studysession.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkit.task.domain.Task;
import org.checkit.task.domain.State;
import org.checkit.task.events.TaskCompletedEvent;
import org.checkit.user.domain.User;
import org.checkit.user.infrastructure.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatsAsyncListener {

    private final UserRepository userRepository;

    @Async
    @EventListener
    public void handleTaskCompleted(TaskCompletedEvent event) {
        Task task = event.getTask();
        User user = task.getUser();

        log.info("Async process initiated for user: {}. Recalculating Jar metrics.", user.getUsername());

        user.setTasksCompleted(user.getTasksCompleted() + 1);

        //logica de las jarras.
        List<Task> userTasks = user.getTasks();
        long completedThisWeek = userTasks.stream()
                .filter(t -> t.getState() == State.COMPLETADA)
                .count();

        long totalThisWeek = userTasks.size();
        double completionRate = totalThisWeek > 0 ? ((double) completedThisWeek / totalThisWeek) * 100 : 0.0;

        log.info("Completaste este porcentaje de tareas esta semana: {}%", completionRate);
        
        userRepository.save(user);
    }
}
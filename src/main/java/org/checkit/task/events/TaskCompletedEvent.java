package org.checkit.task.events;

import lombok.Getter;
import org.checkit.task.domain.Task;

@Getter
public class TaskCompletedEvent {
    private final Task task;

    public TaskCompletedEvent(Task task) {
        this.task = task;
    }
}
package org.checkit.task.infrastructure;

import org.checkit.task.domain.State;
import org.checkit.task.domain.Task;
import org.checkit.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserAndState(User user, State state);
    List<Task> findByUser(User user);
    boolean existsByGoogleCalendarEventId(String googleCalendarEventId);
}

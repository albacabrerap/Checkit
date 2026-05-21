package org.checkit.user.domain;

import lombok.RequiredArgsConstructor;
import org.checkit.exception.BusinessException;
import org.checkit.user.dto.UserConfigUpdateDto;
import org.checkit.user.dto.MobileWidgetResponseDto;
import org.checkit.task.domain.Task;
import org.checkit.task.domain.State;
import org.checkit.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User updateUserPreferences(User user, UserConfigUpdateDto dto) {
        user.setSessionsCompleted(dto.getWeeklyTaskGoal());
        return userRepository.save(user);
    }

    public MobileWidgetResponseDto getWidgetStatusData(User user, int userDefinedGoal) {
        long completedThisWeek = user.getTasks().stream()
                .filter(task -> task.getState() == State.COMPLETADA)
                .count();

        int percentage = (userDefinedGoal > 0) ? (int) ((completedThisWeek * 100) / userDefinedGoal) : 0;
        if (percentage > 100) percentage = 100;

        MobileWidgetResponseDto response = new MobileWidgetResponseDto();
        response.setWeeklyJarPercentage(percentage);

        if (percentage >= 100) {
            response.setMotivationalNote("Completaste tu Jar de la semana!! Felicitaciones");
        } else if (percentage > 50) {
            response.setMotivationalNote("Vas más de la mitad!!! Sigue así y no olvides tomar descansos");
        } else {
            response.setMotivationalNote("Tú puedes! Yo te apoyo!");
        }

        return response;
    }
}
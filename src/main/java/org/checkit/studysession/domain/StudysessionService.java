package org.checkit.studysession.domain;

import lombok.RequiredArgsConstructor;
import org.checkit.distractor.domain.Distractor;
import org.checkit.exception.BusinessException;
import org.checkit.notif.domain.Notif;
import org.checkit.notif.domain.Type;
import org.checkit.notif.infrastructure.NotifRepository;
import org.checkit.studysession.dto.ActiveSessionResponseDto;
import org.checkit.studysession.dto.AddDistractorsToStudysessionDto;
import org.checkit.studysession.infrastructure.StudysessionRepository;
import org.checkit.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudysessionService {

    private final StudysessionRepository studysessionRepository;
    private final NotifRepository notifRepository;

    @Transactional
    public Studysession startSession(User user) {

        Studysession session = new Studysession();
        session.setStartTime(ZonedDateTime.now());
        session.setUser(user);

        return studysessionRepository.save(session);
    }

    public ActiveSessionResponseDto getActiveMobileSessionConfig(User user) {
        ActiveSessionResponseDto dto = new ActiveSessionResponseDto();
        dto.setBlockingActive(true);

        // obtener las apps que el usuario quiera bloquear
        List<String> rules = user.getDistractors().stream()
                .map(Distractor::getUrl)
                .collect(Collectors.toList());
        dto.setRestrictedUrls(rules);
        return dto;
    }

    public Notif triggerInSessionMicroNotification(User user) {
        String[] reminders = {
                "Tiempo de descansar!",
                "Mantente hidratado, toma agua!",
                "Momento de una pausa activa"
        };
        int randomIndex = (int) (Math.random() * reminders.length);

        Notif notification = new Notif();
        notification.setUser(user);
        notification.setMessage(reminders[randomIndex]);
        notification.setType(Type.SESSION);
        notification.setSentAt(ZonedDateTime.now());

        return notifRepository.save(notification);
    }

    public String getWidgetMotivationMessage(User user) {
        int hour = ZonedDateTime.now().getHour();
        if (hour >= 23 || hour < 4) {
            return "Es tarde, hora de ir a descansar";
        }

        if (user.getTasksCompleted() > 3) {
            return "Lo haces increíble";
        }

        return "Tu puedes!";
    }
}
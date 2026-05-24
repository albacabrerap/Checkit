package org.checkit.notif.domain;

import lombok.RequiredArgsConstructor;
import org.checkit.exception.BusinessException;
import org.checkit.notif.infrastructure.NotifRepository;
import org.checkit.user.domain.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotifService {

    private final NotifRepository notifRepository;

    public List<Notif> getByUser(User user) {
        return notifRepository.findByUserOrderBySentAtDesc(user);
    }

    public List<Notif> getUnreadByUser(User user) {
        return notifRepository.findByUserAndReadFalse(user);
    }

    @Async
    public void send(User user, String message, Type type) {
        Notif notif = new Notif();
        notif.setUser(user);
        notif.setMessage(message);
        notif.setType(type);
        notif.setSentAt(ZonedDateTime.now());
        notif.setRead(false);
        notifRepository.save(notif);
    }

    public void markAsRead(Long notifId, User user) {
        Notif notif = notifRepository.findById(notifId)
                .orElseThrow(() -> new BusinessException("No se encontro la notificacion con el id: " + notifId));
        if (!notif.getUser().getId().equals(user.getId())) {
            throw new BusinessException("No tienes los permisos para marcar esto.");
        }
        notif.setRead(true);
        notifRepository.save(notif);
    }

    public void markAllAsRead(User user) {
        List<Notif> unread = notifRepository.findByUserAndReadFalse(user);
        unread.forEach(n -> n.setRead(true));
        notifRepository.saveAll(unread);
    }
}
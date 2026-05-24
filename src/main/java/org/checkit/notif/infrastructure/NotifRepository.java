package org.checkit.notif.infrastructure;

import org.checkit.notif.domain.Notif;
import org.checkit.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifRepository extends JpaRepository<Notif,Long> {
    List<Notif> findByUserAndReadFalse(User user);

    List<Notif> findByUserOrderBySentAtDesc(User user);
}

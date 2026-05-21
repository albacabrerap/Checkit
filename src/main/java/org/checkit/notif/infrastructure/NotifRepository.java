package org.checkit.notif.infrastructure;

import org.checkit.notif.domain.Notif;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifRepository extends JpaRepository<Notif,Long> {
}

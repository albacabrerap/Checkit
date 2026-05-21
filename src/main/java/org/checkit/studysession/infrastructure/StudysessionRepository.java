package org.checkit.studysession.infrastructure;

import org.checkit.studysession.domain.Studysession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudysessionRepository extends JpaRepository<Studysession, Long> {
}

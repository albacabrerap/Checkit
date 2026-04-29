package org.checkit.distractor.infraestructure;

import org.checkit.distractor.domain.Distractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistractorRepository extends JpaRepository<Distractor, Long> {
}

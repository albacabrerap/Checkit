package org.checkit.achivement.infrastructure;


import org.checkit.achivement.domain.Achivement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchivementRepository extends JpaRepository<Achivement,Long> {
}

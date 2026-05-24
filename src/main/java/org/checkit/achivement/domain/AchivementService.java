package org.checkit.achivement.domain;

import lombok.RequiredArgsConstructor;
import org.checkit.exception.BusinessException;
import org.checkit.achivement.infrastructure.AchivementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchivementService {

    private final AchivementRepository achivementRepository;

    public List<Achivement> getAll() {
        return achivementRepository.findAll();
    }

    public Achivement getById(Long id) {
        return achivementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("No se encontró el achivement con el id: " + id));
    }

    public Achivement create(Achivement achivement) {
        return achivementRepository.save(achivement);
    }

    public void delete(Long id) {
        if (!achivementRepository.existsById(id)) {
            throw new BusinessException("No se encontró el achivement con el id: " + id);
        }
        achivementRepository.deleteById(id);
    }

    public List<Achivement> findUnlockable(Condition condition, int currentAmount) {
        return achivementRepository.findAll().stream()
                .filter(a -> a.getCondition() == condition
                        && a.getRequiredAmount() != null
                        && currentAmount >= a.getRequiredAmount())
                .toList();
    }
}
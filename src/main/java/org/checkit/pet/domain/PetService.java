package org.checkit.pet.domain;

import lombok.RequiredArgsConstructor;
import org.checkit.exception.BusinessException;
import org.checkit.pet.dto.PetResponseDto;
import org.checkit.pet.infrastructure.PetRepository;
import org.checkit.user.domain.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void awardExperience(User user, int amount) {
        Pet pet = user.getPet();
        if (pet == null) return;

        pet.setExp(pet.getExp() + amount);
        //100 exp es un nivel
        if (pet.getExp() >= 100) {
            pet.setLevel(pet.getLevel() + (pet.getExp() / 100));
            pet.setExp(pet.getExp() % 100);
        }
        petRepository.save(pet);
    }

    @Transactional
    public PetResponseDto changeSkin(User user, String skinName) {
        Pet pet = user.getPet();
        // las jars pueden desbloquear skins
        pet.setCurrentSkin(skinName);
        return modelMapper.map(petRepository.save(pet), PetResponseDto.class);
    }
}
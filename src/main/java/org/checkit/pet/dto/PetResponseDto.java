package org.checkit.pet.dto;

import lombok.Data;

@Data
public class PetResponseDto {
    private String name;
    private Integer level;
    private Integer exp;
    private String currentSkin;
}
package org.checkit.pet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SelectSkinDto {
    @NotBlank
    private String skinName;
}
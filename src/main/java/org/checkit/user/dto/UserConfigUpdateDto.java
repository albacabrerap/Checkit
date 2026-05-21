package org.checkit.user.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UserConfigUpdateDto {
    @Min(value = 1, message = "El objetivo semanal debe ser mayor a 1")
    private Integer weeklyTaskGoal;

    private String musicPlatformPreference;
    private String externalPlaylistUrl;
}
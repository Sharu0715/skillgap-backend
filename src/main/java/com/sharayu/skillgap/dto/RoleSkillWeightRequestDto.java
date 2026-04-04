package com.sharayu.skillgap.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleSkillWeightRequestDto {

    @NotNull
    private Long roleId;

    @NotNull
    private Long skillId;

    @NotNull
    @Min(value = 0, message = "RequiredLevel cannot be negative")
    @Max(value = 100, message = "RequiredLevel cannot be greater than 100")
    @Column(nullable = false)
    private Integer requiredLevel;
}
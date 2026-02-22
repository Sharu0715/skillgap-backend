package com.sharayu.skillgap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleSkillWeightResponseDto {
    private Long skillId;
    private String skillName;
    private Integer requiredLevel;


}

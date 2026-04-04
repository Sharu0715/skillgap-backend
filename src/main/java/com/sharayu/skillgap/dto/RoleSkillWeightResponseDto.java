package com.sharayu.skillgap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleSkillWeightResponseDto {

    private Long roleId;
    private Long skillId;
    private String skillName;
    private Integer requiredLevel;
}
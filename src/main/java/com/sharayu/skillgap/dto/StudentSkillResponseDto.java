package com.sharayu.skillgap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentSkillResponseDto {
    private Long skillId;
    private String skillName;
    private Integer currentLevel;


}

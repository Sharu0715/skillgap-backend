package com.sharayu.skillgap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class SkillGapDetailDto {
    private String skillName;
    private Integer requiredLevel;
    private Integer currentLevel;
    private  Integer gap;


}

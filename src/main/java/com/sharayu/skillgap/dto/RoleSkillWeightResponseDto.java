package com.sharayu.skillgap.dto;

public class RoleSkillWeightResponseDto {
    private Long skillId;
    private String skillName;
    private Integer requiredLevel;

    public RoleSkillWeightResponseDto(Long skillId, String skillName, Integer requiredLevel) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.requiredLevel = requiredLevel;

    }
    public Long getSkillId() {
        return skillId;
    }
    public String getSkillName() {
        return skillName;
    }
    public Integer getRequiredLevel() {
        return requiredLevel;
    }
}

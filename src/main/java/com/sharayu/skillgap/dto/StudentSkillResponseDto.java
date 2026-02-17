package com.sharayu.skillgap.dto;

public class StudentSkillResponseDto {
    private Long skillId;
    private String skillName;
    private Integer currentLevel;

    public StudentSkillResponseDto(Long skillId, String skillName, Integer currentLevel) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.currentLevel = currentLevel;


    }

    public Long getSkillId() {
        return skillId;
    }

    public String getSkillName() {
    return skillName;
    }
    public Integer getCurrentLevel() {
        return currentLevel;
    }
}

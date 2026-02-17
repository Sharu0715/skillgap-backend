package com.sharayu.skillgap.dto;

public class SkillGapDetailDto {
    private String skillName;
    private Integer requiredLevel;
    private Integer curruntLevel;
    private  Integer gap;

    public SkillGapDetailDto(String skillName, Integer requiredLevel, Integer curruntLevel, Integer gap) {
        this.skillName = skillName;
        this.requiredLevel = requiredLevel;
        this.curruntLevel = curruntLevel;
        this.gap = gap;

    }

    public String getSkillName() {
        return skillName;
    }
    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public Integer getCurruntLevel() {
        return curruntLevel;
    }

    public Integer getGap() {
        return gap;
    }
}

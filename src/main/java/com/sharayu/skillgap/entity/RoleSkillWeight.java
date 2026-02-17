package com.sharayu.skillgap.entity;

import jakarta.persistence.*;

@Entity
public class RoleSkillWeight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer requiredLevel;

    @ManyToOne
    @JoinColumn(name="role_id")
    private JobRole jobRole;
    @ManyToOne
    @JoinColumn(name="skill_id")
    private Skill skill;

    public RoleSkillWeight() {

    }
    public Long getId() {
        return id;
    }
    public Integer getRequiredLevel() {
        return requiredLevel;
    }
    public void setRequiredLevel(Integer requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public JobRole getJobRole() {
        return jobRole;
    }
    public void setJobRole(JobRole jobRole) {
        this.jobRole = jobRole;
    }
    public Skill getSkill() {
        return skill;
    }
    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}

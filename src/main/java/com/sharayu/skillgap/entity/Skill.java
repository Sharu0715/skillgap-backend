package com.sharayu.skillgap.entity;
import jakarta.persistence.*;


@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skillName;
    public Skill(){

    }
    public Long getId() {
        return id;
    }
    public String getSkillName() {
        return skillName;
    }
    public void getSkillName(String skillName) {
        this.skillName=skillName;
    }
}

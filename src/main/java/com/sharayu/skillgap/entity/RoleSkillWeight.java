package com.sharayu.skillgap.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


}

package com.sharayu.skillgap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "role_skill_weight",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"job_role_id", "skill_id"})
        }
)
public class RoleSkillWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Min(value = 0, message = "RequiredLevel cannot be negative")
    @Max(value = 100, message = "RequiredLevel cannot be greater than 100")
    @Column(nullable = false)
    private Integer requiredLevel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_role_id", nullable = false)
    private JobRole jobRole;

    @ManyToOne(optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
}
package com.sharayu.skillgap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="student_skill", uniqueConstraints ={@UniqueConstraint(columnNames = {"student_id","skill_id"})} )
public class StudentSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value =0, message="CurrentLevel cannot be negative")
    @Max(value=100, message="CurrentLevel cannot be greater than 100")
    @Column(nullable = false)
    private Integer currentLevel;

    @ManyToOne(optional = false)
    @JoinColumn(name= "student_id",nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn( name="skill_id",nullable = false)
    private Skill skill;


}

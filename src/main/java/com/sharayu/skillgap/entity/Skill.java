package com.sharayu.skillgap.entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="skill", uniqueConstraints = {@UniqueConstraint(columnNames = "skill_name")})
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false,unique = true)
    private String skillName;

}

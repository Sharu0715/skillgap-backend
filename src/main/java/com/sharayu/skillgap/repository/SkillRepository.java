package com.sharayu.skillgap.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sharayu.skillgap.entity.Skill;
public interface SkillRepository extends JpaRepository<Skill, Long> {


}

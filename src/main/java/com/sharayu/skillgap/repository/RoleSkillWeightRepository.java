package com.sharayu.skillgap.repository;

import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.List;

public interface RoleSkillWeightRepository extends JpaRepository<RoleSkillWeight, Long> {

    List<RoleSkillWeight> findByJobRoleId(Long jobRoleId);

    boolean existsByJobRoleAndSkill(JobRole jobRole, Skill skill);

    Optional<RoleSkillWeight> findByJobRoleIdAndSkillId(Long jobRoleId, Long skillId);

}
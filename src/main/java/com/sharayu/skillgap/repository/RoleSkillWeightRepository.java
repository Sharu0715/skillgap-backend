package com.sharayu.skillgap.repository;

import com.sharayu.skillgap.entity.RoleSkillWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RoleSkillWeightRepository extends JpaRepository<RoleSkillWeight, Long> {
    List<RoleSkillWeight> findByJobRoleId(Long roleId);
}


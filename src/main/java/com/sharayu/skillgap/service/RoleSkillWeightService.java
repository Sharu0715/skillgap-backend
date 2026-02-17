package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.RoleSkillWeightResponseDto;
import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.repository.JobRoleRepository;
import com.sharayu.skillgap.repository.RoleSkillWeightRepository;
import com.sharayu.skillgap.repository.SkillRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class RoleSkillWeightService {

    private final RoleSkillWeightRepository roleSkillWeightRepository;
    private final JobRoleRepository jobRoleRepository;
    private final SkillRepository skillRepository;

    public RoleSkillWeightService(RoleSkillWeightRepository roleSkillWeightRepository,
                                  JobRoleRepository jobRoleRepository,
                                  SkillRepository skillRepository) {
        this.roleSkillWeightRepository = roleSkillWeightRepository;
        this.jobRoleRepository = jobRoleRepository;
        this.skillRepository = skillRepository;
    }

    public RoleSkillWeight addRoleSkill(Long roleId, Long skillId, Integer requiredLevel ) {

        JobRole role = jobRoleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        RoleSkillWeight rsw = new RoleSkillWeight();
        rsw.setJobRole(role);
        rsw.setSkill(skill);
        rsw.setRequiredLevel(requiredLevel);

        return roleSkillWeightRepository.save(rsw);
    }
    public List<RoleSkillWeightResponseDto> getSkillsByRole(Long roleId) {

        List<RoleSkillWeight> list =
                roleSkillWeightRepository.findByJobRoleId(roleId);

        return list.stream()
                .map(rsw -> new RoleSkillWeightResponseDto(
                        rsw.getSkill().getId(),
                        rsw.getSkill().getSkillName(),
                        rsw.getRequiredLevel()
                ))
                .toList();
    }


}

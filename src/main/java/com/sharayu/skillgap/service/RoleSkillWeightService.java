package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.RoleSkillWeightRequestDto;
import com.sharayu.skillgap.dto.RoleSkillWeightResponseDto;
import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.exception.*;
import com.sharayu.skillgap.repository.JobRoleRepository;
import com.sharayu.skillgap.repository.RoleSkillWeightRepository;
import com.sharayu.skillgap.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleSkillWeightService {

    private final RoleSkillWeightRepository roleSkillWeightRepository;
    private final JobRoleRepository jobRoleRepository;
    private final SkillRepository skillRepository;

    public RoleSkillWeightResponseDto addRoleSkill(RoleSkillWeightRequestDto request) {

       JobRole role = jobRoleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " +request.getRoleId()));

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id "+request.getSkillId()));


        if (roleSkillWeightRepository.existsByJobRoleAndSkill(role, skill)) {
            throw new DuplicateResourceException("RoleSkill already exists for roleId: "+request.getRoleId() + " and skillId: " +request.getSkillId());
        }

        RoleSkillWeight roleSkillWeight = new RoleSkillWeight();
        roleSkillWeight.setJobRole(role);
        roleSkillWeight.setSkill(skill);
        roleSkillWeight.setRequiredLevel(request.getRequiredLevel());

        RoleSkillWeight saved = roleSkillWeightRepository.save(roleSkillWeight);

        return new RoleSkillWeightResponseDto(
                saved.getJobRole().getId(),
                saved.getSkill().getId(),
                saved.getSkill().getSkillName(),
                saved.getRequiredLevel()
        );
    }

    @Transactional(readOnly = true)
    public List<RoleSkillWeightResponseDto> getSkillsByRoleId(Long roleId) {

        List<RoleSkillWeight> roleSkills =
                roleSkillWeightRepository.findByJobRoleId(roleId);

        if(roleSkills.isEmpty())
        {
            throw new ResourceNotFoundException("RoleSkill not found with id:  "+roleId);
        }


        return roleSkills.stream()
                .map(rs -> new RoleSkillWeightResponseDto(
                        rs.getJobRole().getId(),
                        rs.getSkill().getId(),
                        rs.getSkill().getSkillName(),
                        rs.getRequiredLevel()
                ))
                .toList();
    }

}
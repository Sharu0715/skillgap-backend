package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.RoleSkillWeightRequestDto;
import com.sharayu.skillgap.dto.RoleSkillWeightResponseDto;
import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.entity.StudentSkill;
import com.sharayu.skillgap.exception.*;
import com.sharayu.skillgap.repository.JobRoleRepository;
import com.sharayu.skillgap.repository.RoleSkillWeightRepository;
import com.sharayu.skillgap.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleSkillWeightService {

    private static final Logger log = LoggerFactory.getLogger(RoleSkillWeightService.class);

    private final RoleSkillWeightRepository roleSkillWeightRepository;
    private final JobRoleRepository jobRoleRepository;
    private final SkillRepository skillRepository;

    public RoleSkillWeightResponseDto addRoleSkill(RoleSkillWeightRequestDto request) {

       JobRole role = jobRoleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " +request.getRoleId()));

       log.info("Role found with id:{} " , role.getId());

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id "+request.getSkillId()));

        log.info("Skill found with id: {}" ,skill.getId());


        if (roleSkillWeightRepository.existsByJobRoleAndSkill(role, skill)) {
            throw new DuplicateResourceException("RoleSkill already exists for roleId: "+request.getRoleId() + " and skillId: " +request.getSkillId());
        }

        RoleSkillWeight roleSkillWeight = new RoleSkillWeight();
        roleSkillWeight.setJobRole(role);
        roleSkillWeight.setSkill(skill);
        roleSkillWeight.setRequiredLevel(request.getRequiredLevel());

        RoleSkillWeight saved = roleSkillWeightRepository.save(roleSkillWeight);

        log.debug("Saving RoleSkill mapping for RoleId:{}, SkillId:{} , SkillName:{}, RequiredLevel:{}" , saved.getJobRole().getId(),saved.getSkill().getId(), saved.getSkill().getSkillName(), saved.getRequiredLevel() );
        log.info("Adding Skill for RoleId:{}, SkillId:{}",  saved.getJobRole().getId(),saved.getSkill().getId());


        return new RoleSkillWeightResponseDto(
                saved.getJobRole().getId(),
                saved.getSkill().getId(),
                saved.getSkill().getSkillName(),
                saved.getRequiredLevel()
        );
    }

    @Transactional(readOnly = true)
    public List<RoleSkillWeightResponseDto> getSkillsByRoleId(Long roleId) {

        log.info("Fetching Skills for roleId:{}",roleId);

        List<RoleSkillWeight> roleSkills =
                roleSkillWeightRepository.findByJobRoleId(roleId);

        log.debug("Roles found:{}", roleSkills.size());

        if(roleSkills.isEmpty())
        {
            throw new ResourceNotFoundException("RoleSkill not found with id:  "+roleId);
        }


        log.info("Successfully fetched skills for roleId:{}",roleId);

        return roleSkills.stream()
                .map(rs -> new RoleSkillWeightResponseDto(
                        rs.getJobRole().getId(),
                        rs.getSkill().getId(),
                        rs.getSkill().getSkillName(),
                        rs.getRequiredLevel()
                ))
                .toList();
    }

    public RoleSkillWeight updateRoleSkill(Long roleId, Long skillId, RoleSkillWeight RoleSkill) {

        RoleSkillWeight existing=roleSkillWeightRepository.findByJobRoleIdAndSkillId(roleId,skillId)
                .orElseThrow(() -> new ResourceNotFoundException("RoleSkills Not found for this roleId : "+roleId +" and skillId : "+skillId));


        existing.setRequiredLevel(RoleSkill.getRequiredLevel());

        log.info("Updating RoleSkill with roleId:{}, skillId {}", roleId, skillId);

        return roleSkillWeightRepository.save(existing);
    }

    public void deleteRoleSkill(Long roleId, Long skillId) {

        RoleSkillWeight existing = roleSkillWeightRepository.findByJobRoleIdAndSkillId(roleId,skillId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("RoleSkill not found for this roleId: "+roleId+" and skillId : "+skillId));

        log.info("Deleting RoleSkill with  roleId:{}, skillIdol {}",roleId,skillId);

        roleSkillWeightRepository.delete(existing);
    }

}
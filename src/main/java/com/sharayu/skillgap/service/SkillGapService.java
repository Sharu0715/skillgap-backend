package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.SkillGapDetailDto;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.StudentSkill;
import com.sharayu.skillgap.repository.RoleSkillWeightRepository;
import com.sharayu.skillgap.repository.StudentSkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkillGapService {
    private RoleSkillWeightRepository roleSkillWeightRepository;
    private StudentSkillRepository studentSkillRepository;


    public SkillGapService( RoleSkillWeightRepository roleSkillWeightRepository,StudentSkillRepository studentSkillRepository) {
        this.roleSkillWeightRepository = roleSkillWeightRepository;
        this.studentSkillRepository = studentSkillRepository;
    }

    public List<SkillGapDetailDto> analyzeSkillGap(Long studentId, Long roleId) {
        List<RoleSkillWeight> rollSkills = roleSkillWeightRepository.findByJobRoleId(roleId);
        List<StudentSkill> studentSkills = studentSkillRepository.findByStudentId(studentId);

        Map<Long, Integer> studentSkillMap= studentSkills.stream().collect(Collectors.toMap(
                ss -> ss.getSkill().getId(),
                StudentSkill::getCurrentLevel
        ));

        List<SkillGapDetailDto> result = new ArrayList<>();

        for(RoleSkillWeight roleSkillWeight:rollSkills) {
            Long skillId = roleSkillWeight.getSkill().getId();
            String skillName = roleSkillWeight.getSkill().getSkillName();
            Integer requiredLevel = roleSkillWeight.getRequiredLevel();

            Integer currentLevel = studentSkillMap.getOrDefault(skillId, 0);

            Integer gap = requiredLevel - currentLevel;

            result.add(new SkillGapDetailDto(
                    skillName,
                    requiredLevel,
                    currentLevel,
                    gap > 0 ? gap : 0
            ));
        }
        return result;

    }
}

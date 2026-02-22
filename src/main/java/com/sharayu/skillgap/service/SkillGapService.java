package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.SkillGapAnalysisResponseDto;
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


    public SkillGapService(RoleSkillWeightRepository roleSkillWeightRepository, StudentSkillRepository studentSkillRepository) {
        this.roleSkillWeightRepository = roleSkillWeightRepository;
        this.studentSkillRepository = studentSkillRepository;
    }

    public SkillGapAnalysisResponseDto analyzeSkillGap(Long studentId, Long roleId) {

        List<RoleSkillWeight> roleSkills =
                roleSkillWeightRepository.findByJobRoleId(roleId);

        List<StudentSkill> studentSkills =
                studentSkillRepository.findByStudentId(studentId);

        Map<Long, Integer> studentSkillMap = studentSkills.stream()
                .collect(Collectors.toMap(
                        ss -> ss.getSkill().getId(),
                        ss -> ss.getCurrentLevel() == null ? 0 : ss.getCurrentLevel()
                ));

        List<SkillGapDetailDto> details = new ArrayList<>();

        int totalRequired = 0;
        int totalAchieved = 0;

        for (RoleSkillWeight rsw : roleSkills) {

            Long skillId = rsw.getSkill().getId();
            String skillName = rsw.getSkill().getSkillName();
            int requiredLevel = rsw.getRequiredLevel();

            int currentLevel = studentSkillMap.getOrDefault(skillId, 0);

            int achieved = Math.min(currentLevel, requiredLevel);
            int gap = requiredLevel - currentLevel;

            totalRequired += requiredLevel;
            totalAchieved += achieved;

            details.add(new SkillGapDetailDto(
                    skillName,
                    requiredLevel,
                    currentLevel,
                    gap > 0 ? gap : 0
            ));
        }

        double matchPercentage =
                totalRequired == 0 ? 0 :
                        ((double) totalAchieved / totalRequired) * 100;

        boolean eligible = matchPercentage >= 70;  // eligibility rule

        String roleName = roleSkills.isEmpty() ?
                "Unknown Role" :
                roleSkills.get(0).getJobRole().getRoleName();

        return new SkillGapAnalysisResponseDto(
                roleName,
                Math.round(matchPercentage * 100.0) / 100.0,
                eligible,
                totalRequired,
                totalAchieved,
                details
        );
    }

}
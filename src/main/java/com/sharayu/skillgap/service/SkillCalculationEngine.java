package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.SkillGapDetailDto;
import com.sharayu.skillgap.dto.SkillGapResult;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.StudentSkill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SkillCalculationEngine {


    @Value("${skill.match.threshold:70}")
    private double threshold;

    private static final Logger log = LoggerFactory.getLogger(SkillCalculationEngine.class);

    public SkillGapResult calculate(
            List<RoleSkillWeight> roleSkills,
            List<StudentSkill> studentSkills
    ) {

        log.debug("Role skills count: {}", roleSkills.size());
        log.debug("Student skills count: {}", studentSkills.size());

        Map<Long, Integer> studentSkillMap =
                studentSkills.stream()
                        .collect(Collectors.toMap(
                                ss -> ss.getSkill().getId(),
                                StudentSkill::getCurrentLevel
                        ));

        log.debug("Student skills map created with {} entries", studentSkillMap.size());

        List<SkillGapDetailDto> details =
                new ArrayList<>();

        int totalRequired = 0;
        int totalAchieved = 0;

        for (RoleSkillWeight rsw : roleSkills) {

            Long skillId = rsw.getSkill().getId();

            String skillName =
                    rsw.getSkill().getSkillName();

            int requiredLevel =
                    rsw.getRequiredLevel();

            int currentLevel =
                    studentSkillMap.getOrDefault(skillId, 0);

            int achieved =
                    Math.min(currentLevel, requiredLevel);

            int gap =
                    Math.max(requiredLevel - currentLevel, 0);

            totalRequired += requiredLevel;
            totalAchieved += achieved;

            details.add(
                    new SkillGapDetailDto(
                            skillName,
                            requiredLevel,
                            currentLevel,
                            gap
                    )
            );
        }

        double matchPercentage =
                totalRequired == 0
                        ? 0
                        : ((double) totalAchieved / totalRequired) * 100;

        boolean eligible =
                matchPercentage >= threshold;

        log.info("Skill Calculation Complete, Match Percentage: {}, eligible: {}", matchPercentage, eligible);
        log.debug("Skill gap details count: {}", details.size());

        return new SkillGapResult(
                Math.round(matchPercentage * 100.0) / 100.0,
                eligible,
                totalRequired,
                totalAchieved,
                details
        );
    }
}
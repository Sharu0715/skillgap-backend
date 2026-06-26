package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.RoleRecommendationDto;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.StudentRepository;
import com.sharayu.skillgap.repository.JobRoleRepository;
import com.sharayu.skillgap.dto.SkillGapAnalysisResponseDto;
import com.sharayu.skillgap.dto.SkillGapDetailDto;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.StudentSkill;
import com.sharayu.skillgap.repository.RoleSkillWeightRepository;
import com.sharayu.skillgap.repository.StudentSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import com.sharayu.skillgap.dto.SkillGapResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillGapService {
    private static final Logger log = LoggerFactory.getLogger(SkillGapService.class);

    private final RoleSkillWeightRepository roleSkillWeightRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final StudentRepository studentRepository;
    private final JobRoleRepository jobRoleRepository;
    private final SkillCalculationEngine skillCalculationEngine;


   /* public SkillGapService(RoleSkillWeightRepository roleSkillWeightRepository, StudentSkillRepository studentSkillRepository,StudentRepository studentRepository, JobRoleRepository jobRoleRepository) {
        this.roleSkillWeightRepository = roleSkillWeightRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.studentRepository = studentRepository;
        this.jobRoleRepository = jobRoleRepository;
    }*/

    public SkillGapAnalysisResponseDto analyzeSkillGap(
             Student student,
            Long roleId
    ) {
         log.info("Starting skill gap analysis for studentId: {}, roleId: {}", student.getId(), roleId);



        JobRole role = jobRoleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found this id: " + roleId));
        log.info("Role found: {}", role.getRoleName());

        List<RoleSkillWeight> roleSkills =
                roleSkillWeightRepository.findByJobRoleId(roleId);

        log.debug("Role skills found: {}", roleSkills.size());


        if (roleSkills.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No skills defined for role: "
                            + role.getRoleName());
        }

        List<StudentSkill> studentSkills =
                studentSkillRepository.findByStudentId(student.getId());

        log.debug("Student skills found: {}", studentSkills.size());


        log.info("Calling SkillCalculationEngine.");
        SkillGapResult result =
                skillCalculationEngine.calculate(
                        roleSkills,
                        studentSkills
                );

        log.info("Skill gap analysis completed. RoleName:{}, MatchPercentage:{},Eligible:{}", role.getRoleName(),result.getMatchPercentage(),result.isEligible());
        log.debug("SkillCalculationEngine result: {}", result.getDetails());

        return new SkillGapAnalysisResponseDto(
                role.getRoleName(),
                result.getMatchPercentage(),
                result.isEligible(),
                result.getTotalRequired(),
                result.getTotalAchieved(),
                result.getDetails()
        );
    }

    public List<RoleRecommendationDto> analyzeAllRoles(Long studentId) {

        log.info("Starting role recommendation for studentId: {}", studentId);
        Student student=studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found this id: "
                                        + studentId));

        log.info("Student found: {}", student.getId());


        List<JobRole> roles =
                jobRoleRepository.findAll();
        log.debug("JobRoles found: {}", roles.size());

        List<RoleRecommendationDto> results =
                new ArrayList<>();

        for (JobRole role : roles) {

            SkillGapAnalysisResponseDto result =
                    analyzeSkillGap(
                            student,
                            role.getId()
                    );

            results.add(
                    new RoleRecommendationDto(
                            result.getRoleName(),
                            result.getMatchPercentage(),
                            result.isEligible()
                    )
            );
        }

        log.info("Sorting role recommendations by MatchPercentage");

        results.sort(
                (a, b) -> Double.compare(
                        b.getMatchPercentage(),
                        a.getMatchPercentage()
                )
        );

        log.info("Generated {} role recommendations", results.size());

        return results;
    }

    public RoleRecommendationDto getBestRole(Long studentId) {

        log.info("Finding  best role for studentId: {}", studentId);

        List<RoleRecommendationDto> roles =
                analyzeAllRoles(studentId);

        if (roles.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No roles found");
        }

        log.info("Best role selected: {}", roles.get(0).getRoleName());

        return roles.get(0);
    }
}
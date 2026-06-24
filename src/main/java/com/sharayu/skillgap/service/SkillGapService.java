package com.sharayu.skillgap.service;

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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillGapService {

    private final RoleSkillWeightRepository roleSkillWeightRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final StudentRepository studentRepository;
    private final JobRoleRepository jobRoleRepository;


   /* public SkillGapService(RoleSkillWeightRepository roleSkillWeightRepository, StudentSkillRepository studentSkillRepository,StudentRepository studentRepository, JobRoleRepository jobRoleRepository) {
        this.roleSkillWeightRepository = roleSkillWeightRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.studentRepository = studentRepository;
        this.jobRoleRepository = jobRoleRepository;
    }*/

    public SkillGapAnalysisResponseDto analyzeSkillGap(Long studentId, Long roleId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found this id: "+studentId));

        JobRole role = jobRoleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found this id: "+roleId));

        List<RoleSkillWeight> roleSkills =
                roleSkillWeightRepository.findByJobRoleId(roleId);

        if (roleSkills.isEmpty()) {
            throw new ResourceNotFoundException("No skills defined for role: " + role.getRoleName());
        }

        List<StudentSkill> studentSkills =
                studentSkillRepository.findByStudentId(studentId);

        Map<Long, Integer> studentSkillMap = studentSkills.stream()
                .collect(Collectors.toMap(
                        ss -> ss.getSkill().getId(),
                        ss -> ss.getCurrentLevel()
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
            int gap = Math.max(requiredLevel - currentLevel, 0);

            totalRequired += requiredLevel;
            totalAchieved += achieved;

            details.add(new SkillGapDetailDto(
                    skillName,
                    requiredLevel,
                    currentLevel,
                    gap
            ));
        }

        double matchPercentage =
                totalRequired == 0 ? 0 :
                        ((double) totalAchieved / totalRequired) * 100;

        boolean eligible = matchPercentage >= 70;

        return new SkillGapAnalysisResponseDto(
                role.getRoleName(),
                Math.round(matchPercentage * 100.0) / 100.0,
                eligible,
                totalRequired,
                totalAchieved,
                details
        );


        }
    public  List<SkillGapAnalysisResponseDto> analyzeAllRoles(Long studentId){
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found this id: "+studentId));

        List<JobRole> roles = jobRoleRepository.findAll();
        List<SkillGapAnalysisResponseDto> results = new ArrayList<>();
        for(JobRole role : roles){
            SkillGapAnalysisResponseDto result= analyzeSkillGap(studentId, role.getId());

            results.add(result);
        }
        results.sort((a,b) ->
                Double.compare(b.getMatchPercentage(), a.getMatchPercentage()));

        return results;
    }

    public SkillGapAnalysisResponseDto getBestRole(Long studentId) {

        List<SkillGapAnalysisResponseDto> roles =
                analyzeAllRoles(studentId);

        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("No roles found");
        }

        return roles.stream()
                .max(Comparator.comparing(SkillGapAnalysisResponseDto::getMatchPercentage))
                .orElseThrow();
    }

}
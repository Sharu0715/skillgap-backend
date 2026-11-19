
package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.SkillGapAnalysisResponseDto;
import com.sharayu.skillgap.dto.SkillGapResult;
import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.JobRoleRepository;
import com.sharayu.skillgap.repository.RoleSkillWeightRepository;
import com.sharayu.skillgap.repository.StudentRepository;
import com.sharayu.skillgap.repository.StudentSkillRepository;
import com.sharayu.skillgap.dto.RoleRecommendationDto;
import static org.mockito.Mockito.inOrder;
import com.sharayu.skillgap.entity.Skill;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillGapServiceTest {

    @Mock
    private RoleSkillWeightRepository roleSkillWeightRepository;

    @Mock
    private StudentSkillRepository studentSkillRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private JobRoleRepository jobRoleRepository;

    @Mock
    private SkillCalculationEngine skillCalculationEngine;

    @Mock
    private GeminiService geminiService;

    @InjectMocks
    private SkillGapService skillGapService;

    @Test
    void analyzeSkillGap_shouldThrowException_whenRoleNotFound() {

        Student student = new Student();
        student.setId(1L);

        when(jobRoleRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> skillGapService.analyzeSkillGap(student, 99L)
        );

        verify(jobRoleRepository).findById(99L);

        verifyNoInteractions(
                roleSkillWeightRepository,
                studentSkillRepository,
                skillCalculationEngine
        );
    }

    @Test
    void analyzeSkillGap_shouldThrowException_whenRoleHasNoSkills() {

        Student student = new Student();
        student.setId(1L);

        JobRole role = new JobRole();
        role.setId(10L);
        role.setRoleName("Java Developer");

        when(jobRoleRepository.findById(10L))
                .thenReturn(Optional.of(role));

        when(roleSkillWeightRepository.findByJobRoleId(10L))
                .thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> skillGapService.analyzeSkillGap(student, 10L)
        );

        assertEquals(
                "No skills defined for role: Java Developer",
                exception.getMessage()
        );

        verify(jobRoleRepository).findById(10L);
        verify(roleSkillWeightRepository).findByJobRoleId(10L);

        verifyNoInteractions(
                studentSkillRepository,
                skillCalculationEngine
        );
    }

    @Test
    void analyzeSkillGap_shouldReturnCorrectResponse() {

        Student student = new Student();
        student.setId(1L);

        JobRole role = new JobRole();
        role.setId(10L);
        role.setRoleName("Java Developer");

        RoleSkillWeight roleSkill = mock(RoleSkillWeight.class);

        // Mock repository responses
        when(jobRoleRepository.findById(10L))
                .thenReturn(Optional.of(role));

        when(roleSkillWeightRepository.findByJobRoleId(10L))
                .thenReturn(List.of(roleSkill));

        when(studentSkillRepository.findByStudentId(1L))
                .thenReturn(List.of());

        // SkillCalculationEngine result
        SkillGapResult result = mock(SkillGapResult.class);

        when(result.getMatchPercentage()).thenReturn(85.71);
        when(result.isEligible()).thenReturn(true);
        when(result.getTotalRequired()).thenReturn(140);
        when(result.getTotalAchieved()).thenReturn(120);
        when(result.getDetails()).thenReturn(List.of());

        when(skillCalculationEngine.calculate(
                List.of(roleSkill),
                List.of()
        )).thenReturn(result);

        // Execute service method
        SkillGapAnalysisResponseDto response =
                skillGapService.analyzeSkillGap(student, 10L);

        // Verify response
        assertNotNull(response);
        assertEquals("Java Developer", response.getRoleName());
        assertEquals(85.71, response.getMatchPercentage(), 0.001);
        assertTrue(response.isEligible());
        assertEquals(140, response.getTotalRequired());
        assertEquals(120, response.getTotalAchieved());
        assertNotNull(response.getDetails());

        // Verify repository and engine calls
        verify(jobRoleRepository).findById(10L);
        verify(roleSkillWeightRepository).findByJobRoleId(10L);
        verify(studentSkillRepository).findByStudentId(1L);
        verify(skillCalculationEngine).calculate(
                List.of(roleSkill),
                List.of()
        );
    }

    @Test
    void analyzeAllRoles_shouldThrowException_whenStudentNotFound() {

        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> skillGapService.analyzeAllRoles(99L)
        );

        verify(studentRepository).findById(99L);
        verifyNoInteractions(
                jobRoleRepository,
                roleSkillWeightRepository,
                studentSkillRepository,
                skillCalculationEngine
        );
    }

    @Test
    void analyzeAllRoles_shouldReturnEmptyList_whenNoRolesExist() {

        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(jobRoleRepository.findAll())
                .thenReturn(List.of());

        List<RoleRecommendationDto> results =
                skillGapService.analyzeAllRoles(1L);

        assertNotNull(results);
        assertTrue(results.isEmpty());

        verify(studentRepository).findById(1L);
        verify(jobRoleRepository).findAll();
        verifyNoInteractions(
                roleSkillWeightRepository,
                studentSkillRepository,
                skillCalculationEngine
        );
    }

    @Test
    void analyzeAllRoles_shouldSortRolesByMatchPercentageDescending() {

        Student student = new Student();
        student.setId(1L);

        JobRole javaRole = new JobRole();
        javaRole.setId(10L);
        javaRole.setRoleName("Java Developer");

        JobRole pythonRole = new JobRole();
        pythonRole.setId(20L);
        pythonRole.setRoleName("Python Developer");

        RoleSkillWeight javaSkill = mock(RoleSkillWeight.class);
        RoleSkillWeight pythonSkill = mock(RoleSkillWeight.class);

        SkillGapResult javaResult = mock(SkillGapResult.class);
        when(javaResult.getMatchPercentage()).thenReturn(85.0);
        when(javaResult.isEligible()).thenReturn(true);
        when(javaResult.getTotalRequired()).thenReturn(100);
        when(javaResult.getTotalAchieved()).thenReturn(85);
        when(javaResult.getDetails()).thenReturn(List.of());

        SkillGapResult pythonResult = mock(SkillGapResult.class);
        when(pythonResult.getMatchPercentage()).thenReturn(65.0);
        when(pythonResult.isEligible()).thenReturn(false);
        when(pythonResult.getTotalRequired()).thenReturn(100);
        when(pythonResult.getTotalAchieved()).thenReturn(65);
        when(pythonResult.getDetails()).thenReturn(List.of());

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(jobRoleRepository.findAll())
                .thenReturn(List.of(pythonRole, javaRole));

        when(jobRoleRepository.findById(20L))
                .thenReturn(Optional.of(pythonRole));
        when(jobRoleRepository.findById(10L))
                .thenReturn(Optional.of(javaRole));

        when(roleSkillWeightRepository.findByJobRoleId(20L))
                .thenReturn(List.of(pythonSkill));
        when(roleSkillWeightRepository.findByJobRoleId(10L))
                .thenReturn(List.of(javaSkill));

        when(studentSkillRepository.findByStudentId(1L))
                .thenReturn(List.of());

        when(skillCalculationEngine.calculate(
                List.of(pythonSkill), List.of()))
                .thenReturn(pythonResult);

        when(skillCalculationEngine.calculate(
                List.of(javaSkill), List.of()))
                .thenReturn(javaResult);

        List<RoleRecommendationDto> results =
                skillGapService.analyzeAllRoles(1L);

        assertEquals(2, results.size());

        assertEquals("Java Developer", results.get(0).getRoleName());
        assertEquals(85.0, results.get(0).getMatchPercentage(), 0.001);
        assertTrue(results.get(0).isEligible());

        assertEquals("Python Developer", results.get(1).getRoleName());
        assertEquals(65.0, results.get(1).getMatchPercentage(), 0.001);
        assertFalse(results.get(1).isEligible());
    }

    @Test
    void getBestRole_shouldReturnRoleWithHighestMatchPercentage() {

        Student student = new Student();
        student.setId(1L);

        JobRole javaRole = new JobRole();
        javaRole.setId(10L);
        javaRole.setRoleName("Java Developer");

        JobRole pythonRole = new JobRole();
        pythonRole.setId(20L);
        pythonRole.setRoleName("Python Developer");

        RoleSkillWeight javaSkill = mock(RoleSkillWeight.class);
        RoleSkillWeight pythonSkill = mock(RoleSkillWeight.class);

        SkillGapResult javaResult = mock(SkillGapResult.class);
        when(javaResult.getMatchPercentage()).thenReturn(90.0);
        when(javaResult.isEligible()).thenReturn(true);
        when(javaResult.getTotalRequired()).thenReturn(100);
        when(javaResult.getTotalAchieved()).thenReturn(90);
        when(javaResult.getDetails()).thenReturn(List.of());

        SkillGapResult pythonResult = mock(SkillGapResult.class);
        when(pythonResult.getMatchPercentage()).thenReturn(60.0);
        when(pythonResult.isEligible()).thenReturn(false);
        when(pythonResult.getTotalRequired()).thenReturn(100);
        when(pythonResult.getTotalAchieved()).thenReturn(60);
        when(pythonResult.getDetails()).thenReturn(List.of());

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(jobRoleRepository.findAll())
                .thenReturn(List.of(pythonRole, javaRole));

        when(jobRoleRepository.findById(20L))
                .thenReturn(Optional.of(pythonRole));
        when(jobRoleRepository.findById(10L))
                .thenReturn(Optional.of(javaRole));

        when(roleSkillWeightRepository.findByJobRoleId(20L))
                .thenReturn(List.of(pythonSkill));
        when(roleSkillWeightRepository.findByJobRoleId(10L))
                .thenReturn(List.of(javaSkill));

        when(studentSkillRepository.findByStudentId(1L))
                .thenReturn(List.of());

        when(skillCalculationEngine.calculate(List.of(pythonSkill), List.of()))
                .thenReturn(pythonResult);
        when(skillCalculationEngine.calculate(List.of(javaSkill), List.of()))
                .thenReturn(javaResult);

        RoleRecommendationDto bestRole =
                skillGapService.getBestRole(1L);

        assertNotNull(bestRole);
        assertEquals("Java Developer", bestRole.getRoleName());
        assertEquals(90.0, bestRole.getMatchPercentage(), 0.001);
        assertTrue(bestRole.isEligible());
    }

    @Test
    void getBestRole_shouldThrowException_whenNoRolesExist() {

        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(jobRoleRepository.findAll())
                .thenReturn(List.of());

        assertThrows(
                ResourceNotFoundException.class,
                () -> skillGapService.getBestRole(1L)
        );

        verify(studentRepository).findById(1L);
        verify(jobRoleRepository).findAll();
    }


    @Test
    void getAiRecommendation_shouldReturnGeminiResponse() {

        Student student = new Student();
        student.setId(1L);

        JobRole role = new JobRole();
        role.setId(10L);
        role.setRoleName("Java Developer");

        Skill javaSkill = new Skill();
        javaSkill.setId(1L);
        javaSkill.setSkillName("Java");

        RoleSkillWeight roleSkill = mock(RoleSkillWeight.class);

        when(roleSkill.getSkill()).thenReturn(javaSkill);
        when(roleSkill.getRequiredLevel()).thenReturn(80);

        SkillGapResult result = mock(SkillGapResult.class);
        when(result.getMatchPercentage()).thenReturn(90.0);
        when(result.isEligible()).thenReturn(true);
        when(result.getTotalRequired()).thenReturn(100);
        when(result.getTotalAchieved()).thenReturn(90);
        when(result.getDetails()).thenReturn(List.of());

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(jobRoleRepository.findAll())
                .thenReturn(List.of(role));

        when(jobRoleRepository.findById(10L))
                .thenReturn(Optional.of(role));

        when(roleSkillWeightRepository.findByJobRoleId(10L))
                .thenReturn(List.of(roleSkill));

        when(studentSkillRepository.findByStudentId(1L))
                .thenReturn(List.of());

        when(skillCalculationEngine.calculate(
                List.of(roleSkill), List.of()))
                .thenReturn(result);

        when(geminiService.getRecommendation(anyString()))
                .thenReturn("Improve Java, Spring Boot and SQL skills.");

        String recommendation =
                skillGapService.getAiRecommendation(1L);

        assertNotNull(recommendation);
        assertEquals(
                "Improve Java, Spring Boot and SQL skills.",
                recommendation
        );

        verify(geminiService).getRecommendation(anyString());


    }


    @Test
    void getAiRecommendation_shouldThrowException_whenNoRolesExist() {

        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(jobRoleRepository.findAll())
                .thenReturn(List.of());

        assertThrows(
                ResourceNotFoundException.class,
                () -> skillGapService.getAiRecommendation(1L)
        );

        verify(studentRepository).findById(1L);
        verify(jobRoleRepository).findAll();

        verifyNoInteractions(
                roleSkillWeightRepository,
                studentSkillRepository,
                skillCalculationEngine,
                geminiService
        );
    }

}

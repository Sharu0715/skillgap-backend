
package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.RoleSkillWeightRequestDto;
import com.sharayu.skillgap.dto.RoleSkillWeightResponseDto;
import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.JobRoleRepository;
import com.sharayu.skillgap.repository.RoleSkillWeightRepository;
import com.sharayu.skillgap.repository.SkillRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleSkillWeightServiceTest {

    @Mock
    private RoleSkillWeightRepository roleSkillWeightRepository;

    @Mock
    private JobRoleRepository jobRoleRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private RoleSkillWeightService roleSkillWeightService;

    @Test
    void addRoleSkill_shouldThrowException_whenRoleNotFound() {

        RoleSkillWeightRequestDto request = new RoleSkillWeightRequestDto();
        request.setRoleId(99L);
        request.setSkillId(1L);
        request.setRequiredLevel(80);

        when(jobRoleRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> roleSkillWeightService.addRoleSkill(request)
        );

        verify(skillRepository, never()).findById(any());
        verify(roleSkillWeightRepository, never()).save(any(RoleSkillWeight.class));
    }

    @Test
    void addRoleSkill_shouldThrowException_whenSkillNotFound() {

        RoleSkillWeightRequestDto request = new RoleSkillWeightRequestDto();
        request.setRoleId(1L);
        request.setSkillId(99L);
        request.setRequiredLevel(80);

        JobRole jobRole = new JobRole();
        jobRole.setId(1L);
        jobRole.setRoleName("Java Developer");

        when(jobRoleRepository.findById(1L))
                .thenReturn(Optional.of(jobRole));

        when(skillRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> roleSkillWeightService.addRoleSkill(request)
        );

        verify(roleSkillWeightRepository, never())
                .save(any(RoleSkillWeight.class));
    }

    @Test
    void addRoleSkill_shouldThrowException_whenMappingAlreadyExists() {

        RoleSkillWeightRequestDto request = new RoleSkillWeightRequestDto();
        request.setRoleId(1L);
        request.setSkillId(1L);
        request.setRequiredLevel(80);

        JobRole jobRole = new JobRole();
        jobRole.setId(1L);
        jobRole.setRoleName("Java Developer");

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        when(jobRoleRepository.findById(1L))
                .thenReturn(Optional.of(jobRole));

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        when(roleSkillWeightRepository.existsByJobRoleAndSkill(jobRole, skill))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> roleSkillWeightService.addRoleSkill(request)
        );

        verify(roleSkillWeightRepository, never())
                .save(any(RoleSkillWeight.class));
    }

    @Test
    void addRoleSkill_shouldSaveSuccessfully_whenMappingDoesNotExist() {

        RoleSkillWeightRequestDto request = new RoleSkillWeightRequestDto();
        request.setRoleId(1L);
        request.setSkillId(1L);
        request.setRequiredLevel(80);

        JobRole jobRole = new JobRole();
        jobRole.setId(1L);
        jobRole.setRoleName("Java Developer");

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        when(jobRoleRepository.findById(1L))
                .thenReturn(Optional.of(jobRole));

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        when(roleSkillWeightRepository.existsByJobRoleAndSkill(jobRole, skill))
                .thenReturn(false);

        when(roleSkillWeightRepository.save(any(RoleSkillWeight.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RoleSkillWeightResponseDto result =
                roleSkillWeightService.addRoleSkill(request);

        assertEquals(1L, result.getRoleId());
        assertEquals(1L, result.getSkillId());
        assertEquals("Java", result.getSkillName());
        assertEquals(80, result.getRequiredLevel());

        verify(roleSkillWeightRepository).save(any(RoleSkillWeight.class));
    }

    @Test
    void getSkillsByRoleId_shouldThrowException_whenNoMappingsFound() {

        when(roleSkillWeightRepository.findByJobRoleId(99L))
                .thenReturn(java.util.Collections.emptyList());

        assertThrows(
                ResourceNotFoundException.class,
                () -> roleSkillWeightService.getSkillsByRoleId(99L)
        );

        verify(roleSkillWeightRepository).findByJobRoleId(99L);
    }


    @Test
    void getSkillsByRoleId_shouldReturnMappedSkills() {

        JobRole jobRole = new JobRole();
        jobRole.setId(1L);
        jobRole.setRoleName("Java Developer");

        Skill skill1 = new Skill();
        skill1.setId(1L);
        skill1.setSkillName("Java");

        Skill skill2 = new Skill();
        skill2.setId(2L);
        skill2.setSkillName("Spring Boot");

        RoleSkillWeight mapping1 = new RoleSkillWeight();
        mapping1.setJobRole(jobRole);
        mapping1.setSkill(skill1);
        mapping1.setRequiredLevel(80);

        RoleSkillWeight mapping2 = new RoleSkillWeight();
        mapping2.setJobRole(jobRole);
        mapping2.setSkill(skill2);
        mapping2.setRequiredLevel(90);

        when(roleSkillWeightRepository.findByJobRoleId(1L))
                .thenReturn(java.util.List.of(mapping1, mapping2));

        List<RoleSkillWeightResponseDto> result =
                roleSkillWeightService.getSkillsByRoleId(1L);

        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getRoleId());
        assertEquals(1L, result.get(0).getSkillId());
        assertEquals("Java", result.get(0).getSkillName());
        assertEquals(80, result.get(0).getRequiredLevel());

        assertEquals(2L, result.get(1).getSkillId());
        assertEquals("Spring Boot", result.get(1).getSkillName());
        assertEquals(90, result.get(1).getRequiredLevel());

        verify(roleSkillWeightRepository).findByJobRoleId(1L);
    }


    @Test
    void updateRoleSkill_shouldUpdateRequiredLevel_whenMappingExists() {

        JobRole jobRole = new JobRole();
        jobRole.setId(1L);
        jobRole.setRoleName("Java Developer");

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        RoleSkillWeight existingMapping = new RoleSkillWeight();
        existingMapping.setJobRole(jobRole);
        existingMapping.setSkill(skill);
        existingMapping.setRequiredLevel(70);

        RoleSkillWeight updatedMapping = new RoleSkillWeight();
        updatedMapping.setRequiredLevel(90);

        when(roleSkillWeightRepository.findByJobRoleIdAndSkillId(1L, 1L))
                .thenReturn(java.util.Optional.of(existingMapping));

        when(roleSkillWeightRepository.save(existingMapping))
                .thenReturn(existingMapping);

        RoleSkillWeight result = roleSkillWeightService.updateRoleSkill(
                1L, 1L, updatedMapping
        );

        assertEquals(90, result.getRequiredLevel());

        verify(roleSkillWeightRepository).findByJobRoleIdAndSkillId(1L, 1L);
        verify(roleSkillWeightRepository).save(existingMapping);
    }


    @Test
    void deleteRoleSkill_shouldThrowException_whenMappingNotFound() {

        when(roleSkillWeightRepository.findByJobRoleIdAndSkillId(99L, 88L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> roleSkillWeightService.deleteRoleSkill(99L, 88L)
        );

        verify(roleSkillWeightRepository, never())
                .delete(any(RoleSkillWeight.class));
    }


    @Test
    void deleteRoleSkill_shouldDeleteSuccessfully_whenMappingExists() {

        JobRole jobRole = new JobRole();
        jobRole.setId(1L);
        jobRole.setRoleName("Java Developer");









        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        RoleSkillWeight existingMapping = new RoleSkillWeight();
        existingMapping.setJobRole(jobRole);
        existingMapping.setSkill(skill);
        existingMapping.setRequiredLevel(80);

        when(roleSkillWeightRepository.findByJobRoleIdAndSkillId(1L, 1L))
                .thenReturn(java.util.Optional.of(existingMapping));

        roleSkillWeightService.deleteRoleSkill(1L, 1L);

        verify(roleSkillWeightRepository)
                .findByJobRoleIdAndSkillId(1L, 1L);

        verify(roleSkillWeightRepository).delete(existingMapping);
    }



}

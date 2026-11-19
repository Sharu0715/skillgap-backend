
package com.sharayu.skillgap.service;

import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    @Test
    void addSkill_shouldSaveSkillSuccessfully_whenSkillDoesNotExist() {

        Skill skill = new Skill();
        skill.setSkillName("Spring Boot");

        when(skillRepository.existsBySkillName("Spring Boot"))
                .thenReturn(false);

        when(skillRepository.save(skill))
                .thenAnswer(invocation -> {
                    Skill savedSkill = invocation.getArgument(0);
                    savedSkill.setId(1L);
                    return savedSkill;
                });

        Skill result = skillService.addSkill(skill);

        assertEquals(1L, result.getId());
        assertEquals("Spring Boot", result.getSkillName());

        verify(skillRepository).existsBySkillName("Spring Boot");
        verify(skillRepository).save(skill);
    }
    @Test
    void getSkillById_shouldThrowException_whenSkillNotFound() {

        Long skillId = 1L;

        when(skillRepository.findById(skillId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> skillService.getSkillById(skillId)
        );

        verify(skillRepository).findById(skillId);
    }
    @Test
    void getSkillById_shouldReturnSkill_whenSkillExists() {

        Long skillId = 1L;

        Skill skill = new Skill();
        skill.setId(skillId);
        skill.setSkillName("Java");

        when(skillRepository.findById(skillId))
                .thenReturn(Optional.of(skill));

        Skill result = skillService.getSkillById(skillId);

        assertEquals(skillId, result.getId());
        assertEquals("Java", result.getSkillName());

        verify(skillRepository).findById(skillId);
    }
    @Test
    void getAllSkills_shouldReturnPaginatedSkills() {

        Skill skill1 = new Skill();
        skill1.setId(1L);
        skill1.setSkillName("Java");

        Skill skill2 = new Skill();
        skill2.setId(2L);
        skill2.setSkillName("Spring Boot");

        Pageable pageable = PageRequest.of(0, 10);

        Page<Skill> skillPage = new PageImpl<>(
                List.of(skill1, skill2),
                pageable,
                2
        );

        when(skillRepository.findAll(pageable))
                .thenReturn(skillPage);

        Page<Skill> result = skillService.getAllSkills(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Java", result.getContent().get(0).getSkillName());
        assertEquals("Spring Boot", result.getContent().get(1).getSkillName());

        verify(skillRepository).findAll(pageable);
    }
    @Test
    void getAllSkillsForDropdown_shouldReturnAllSkills() {

        Skill skill1 = new Skill();
        skill1.setId(1L);
        skill1.setSkillName("Java");

        Skill skill2 = new Skill();
        skill2.setId(2L);
        skill2.setSkillName("Spring Boot");

        when(skillRepository.findAll())
                .thenReturn(List.of(skill1, skill2));

        List<Skill> result = skillService.getAllSkillsForDropdown();

        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getSkillName());
        assertEquals("Spring Boot", result.get(1).getSkillName());

        verify(skillRepository).findAll();
    }
    @Test
    void updateSkill_shouldThrowException_whenSkillNotFound() {

        Skill updatedSkill = new Skill();
        updatedSkill.setSkillName("Spring Boot");

        when(skillRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> skillService.updateSkill(99L, updatedSkill)
        );

        verify(skillRepository, never()).save(any(Skill.class));
    }
    @Test
    void updateSkill_shouldUpdateSkillNameSuccessfully() {

        Skill existingSkill = new Skill();
        existingSkill.setId(1L);
        existingSkill.setSkillName("Java");

        Skill updatedSkill = new Skill();
        updatedSkill.setSkillName("Advanced Java");

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(existingSkill));

        when(skillRepository.save(any(Skill.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Skill result = skillService.updateSkill(1L, updatedSkill);

        assertEquals(1L, result.getId());
        assertEquals("Advanced Java", result.getSkillName());

        verify(skillRepository).findById(1L);
        verify(skillRepository).save(existingSkill);
    }

    @Test
    void deleteSkill_shouldThrowException_whenSkillNotFound() {

        when(skillRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> skillService.deleteSkill(99L)
        );

        verify(skillRepository, never()).delete(any(Skill.class));
    }
    @Test
    void deleteSkill_shouldDeleteSkillSuccessfully() {

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        skillService.deleteSkill(1L);

        verify(skillRepository).findById(1L);
        verify(skillRepository).delete(skill);
    }
}
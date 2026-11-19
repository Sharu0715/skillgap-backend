
package com.sharayu.skillgap.service;

import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.JobRoleRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobRoleServiceTest {

    @Mock
    private JobRoleRepository jobRoleRepository;

    @InjectMocks
    private JobRoleService jobRoleService;

    @Test
    void addJobRole_shouldThrowException_whenRoleAlreadyExists() {

        JobRole jobRole = new JobRole();
        jobRole.setRoleName("Java Developer");

        when(jobRoleRepository.existsByRoleName("Java Developer"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> jobRoleService.addjobRole(jobRole)
        );

        verify(jobRoleRepository, never()).save(any(JobRole.class));
    }

    @Test
    void addJobRole_shouldSaveSuccessfully_whenRoleDoesNotExist() {

        JobRole jobRole = new JobRole();
        jobRole.setRoleName("Python Developer");

        when(jobRoleRepository.existsByRoleName("Python Developer"))
                .thenReturn(false);

        when(jobRoleRepository.save(jobRole))
                .thenAnswer(invocation -> {
                    JobRole savedRole = invocation.getArgument(0);
                    savedRole.setId(1L);
                    return savedRole;
                });

        JobRole result = jobRoleService.addjobRole(jobRole);

        assertEquals(1L, result.getId());
        assertEquals("Python Developer", result.getRoleName());

        verify(jobRoleRepository).existsByRoleName("Python Developer");
        verify(jobRoleRepository).save(jobRole);
    }

    @Test
    void getJobRolesById_shouldThrowException_whenRoleNotFound() {

        when(jobRoleRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> jobRoleService.getJobRolesById(99L)
        );

        verify(jobRoleRepository).findById(99L);
    }

    @Test
    void getJobRolesById_shouldReturnRole_whenRoleExists() {

        JobRole jobRole = new JobRole();
        jobRole.setId(1L);
        jobRole.setRoleName("Java Developer");

        when(jobRoleRepository.findById(1L))
                .thenReturn(java.util.Optional.of(jobRole));

        JobRole result = jobRoleService.getJobRolesById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Java Developer", result.getRoleName());

        verify(jobRoleRepository).findById(1L);
    }

    @Test
    void getAllJobRoles_shouldReturnPaginatedRoles() {

        JobRole role1 = new JobRole();
        role1.setId(1L);
        role1.setRoleName("Java Developer");

        JobRole role2 = new JobRole();
        role2.setId(2L);
        role2.setRoleName("Python Developer");

        Pageable pageable = PageRequest.of(0, 10);

        Page<JobRole> rolePage = new PageImpl<>(
                java.util.List.of(role1, role2),
                pageable,
                2
        );

        when(jobRoleRepository.findAll(pageable))
                .thenReturn(rolePage);

        Page<JobRole> result = jobRoleService.getAllJobRoles(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Java Developer", result.getContent().get(0).getRoleName());
        assertEquals("Python Developer", result.getContent().get(1).getRoleName());

        verify(jobRoleRepository).findAll(pageable);
    }

    @Test
    void updateJobRole_shouldThrowException_whenRoleNotFound() {

        JobRole updatedRole = new JobRole();
        updatedRole.setRoleName("Senior Java Developer");

        when(jobRoleRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> jobRoleService.updateJobRole(99L, updatedRole)
        );

        verify(jobRoleRepository, never()).save(any(JobRole.class));
    }

    @Test
    void updateJobRole_shouldUpdateSuccessfully_whenRoleExists() {

        JobRole existingRole = new JobRole();
        existingRole.setId(1L);
        existingRole.setRoleName("Java Developer");

        JobRole updatedRole = new JobRole();
        updatedRole.setRoleName("Senior Java Developer");

        when(jobRoleRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existingRole));

        when(jobRoleRepository.save(any(JobRole.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobRole result = jobRoleService.updateJobRole(1L, updatedRole);

        assertEquals(1L, result.getId());
        assertEquals("Senior Java Developer", result.getRoleName());

        verify(jobRoleRepository).findById(1L);
        verify(jobRoleRepository).save(existingRole);
    }

    @Test
    void deleteJobRole_shouldThrowException_whenRoleNotFound() {

        when(jobRoleRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> jobRoleService.deleteJobRole(99L)
        );

        verify(jobRoleRepository, never()).delete(any(JobRole.class));
    }




    @Test
    void deleteJobRole_shouldDeleteSuccessfully_whenRoleExists() {

        JobRole jobRole = new JobRole();
        jobRole.setId(1L);
        jobRole.setRoleName("Java Developer");

        when(jobRoleRepository.findById(1L))
                .thenReturn(java.util.Optional.of(jobRole));

        jobRoleService.deleteJobRole(1L);

        verify(jobRoleRepository).findById(1L);
        verify(jobRoleRepository).delete(jobRole);
    }


}

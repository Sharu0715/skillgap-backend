package com.sharayu.skillgap.service;

import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class JobRoleService {
    private final JobRoleRepository jobRoleRepository;
    /*public JobRoleService(JobRoleRepository jobRoleRepository){
        this.jobRoleRepository=jobRoleRepository;
    }*/
    public JobRole addjobRole(JobRole jobRole){
        if (jobRoleRepository.existsByRoleName(jobRole.getRoleName())) {
            throw new DuplicateResourceException("Role already exists with role name: " + jobRole.getRoleName());
        }
        return jobRoleRepository.save(jobRole);
    }
    public JobRole getJobRolesById(Long id) {
        return jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " +id));
    }
}

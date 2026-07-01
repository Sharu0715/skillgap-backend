package com.sharayu.skillgap.service;

import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class JobRoleService {
    //private static final Logger log = LoggerFactory.getLogger(JobRoleService.class);
    private final JobRoleRepository jobRoleRepository;

    /*public JobRoleService(JobRoleRepository jobRoleRepository){
        this.jobRoleRepository=jobRoleRepository;
    }*/
    public JobRole addjobRole(JobRole jobRole) {
        if (jobRoleRepository.existsByRoleName(jobRole.getRoleName())) {
            throw new DuplicateResourceException("JobRole already exists with role name: " + jobRole.getRoleName());
        }
        log.info("Creating JobRole with name:{}", jobRole.getRoleName());
        JobRole saved = jobRoleRepository.save(jobRole);
        log.info("JobRole created successfully with id:{}", saved.getId());
        return saved;

    }

    public JobRole getJobRolesById(Long id) {
        log.info("Fetching JobRole by id:{}", id);
        return jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobRole not found with id: " + id));
    }

    public List<JobRole> getAllJobRoles() {

        log.info("Fetching all JobRoles");

        return jobRoleRepository.findAll();
    }

    public JobRole updateJobRole(Long id, JobRole jobRole) {

        JobRole existing=jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobRole Not found for this id: "+id));

         existing.setRoleName(jobRole.getRoleName());

         log.info("Updating JobRole with name:{}", jobRole.getRoleName());

        return jobRoleRepository.save(existing);
    }

    public void deleteJobRole(Long id) {

        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("JobRole not found for this id: "+id));

        log.info("JobRole found successfully with id:{}",jobRole.getId());

        jobRoleRepository.delete(jobRole);
    }
}



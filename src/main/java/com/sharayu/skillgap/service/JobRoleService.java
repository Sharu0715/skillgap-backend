package com.sharayu.skillgap.service;

import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.repository.JobRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobRoleService {
    private JobRoleRepository jobRoleRepository;
    public JobRoleService(JobRoleRepository jobRoleRepository){
        this.jobRoleRepository=jobRoleRepository;
    }
    public JobRole save(JobRole jobRole){
        return jobRoleRepository.save(jobRole);
    }
    public List<JobRole> getAllJobRoles(){
        return jobRoleRepository.findAll();
    }
}

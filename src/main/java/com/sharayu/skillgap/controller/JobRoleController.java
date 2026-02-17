package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.service.JobRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/jobrolls")
public class JobRoleController {
    private final JobRoleService jobRoleService;
    public JobRoleController(JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
    }
    @PostMapping
    public JobRole createJobRole(@RequestBody JobRole jobRole) {
        return jobRoleService.save(jobRole);
    }

    @GetMapping
    public List<JobRole> getAllJobRoles() {
        return jobRoleService.getAllJobRoles();
    }
}

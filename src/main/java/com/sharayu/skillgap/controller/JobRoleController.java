package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.service.JobRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@RestController
@RequestMapping("api/jobRolls")
@RequiredArgsConstructor

public class JobRoleController {
    private final JobRoleService jobRoleService;
    /*public JobRoleController(JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
    }*/
    @PostMapping
    public JobRole createJobRole(@RequestBody JobRole jobRole) {
        return jobRoleService.addjobRole(jobRole);
    }

    @GetMapping("{id}")
    public JobRole getJobRolesById(@PathVariable Long id) {
        return jobRoleService.getJobRolesById(id);
    }
}

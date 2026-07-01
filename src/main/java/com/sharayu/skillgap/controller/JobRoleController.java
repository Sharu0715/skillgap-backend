package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.service.JobRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@RestController
@RequestMapping("api/jobRoles")
@RequiredArgsConstructor

public class JobRoleController {
    private static final Logger log = LoggerFactory.getLogger(JobRoleController.class);
    private final JobRoleService jobRoleService;
    /*public JobRoleController(JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
    }*/
    @PostMapping
    public JobRole createJobRole(@RequestBody JobRole jobRole) {
        log.info("Received request to create job role: {}", jobRole);
        return jobRoleService.addjobRole(jobRole);
    }

    @GetMapping("{id}")
    public JobRole getJobRolesById(@PathVariable Long id) {
        log.info("Received request to get Job roles by id: {}", id);
        return jobRoleService.getJobRolesById(id);
    }

    @GetMapping
    public ResponseEntity<List<JobRole>> getAllJobRoles() {

        log.info("Received request to get all job roles");

        return ResponseEntity.ok(jobRoleService.getAllJobRoles());
    }

    @PutMapping("/{id}")
    public JobRole updateJobRole(@PathVariable Long id, @RequestBody JobRole jobRole) {

        log.info("Received request to update job role: {}", id);

        return jobRoleService.updateJobRole(id, jobRole);
    }


    @DeleteMapping("/{id}")
    public String deleteJobRole(@PathVariable Long id) {

        log.info("Received request to delete job role: {}", id);

        jobRoleService.deleteJobRole(id);
        return "JobRole deleted successfully";
    }
}

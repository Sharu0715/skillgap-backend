package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.dto.RoleSkillWeightRequestDto;
import com.sharayu.skillgap.dto.RoleSkillWeightResponseDto;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.StudentSkill;
import com.sharayu.skillgap.service.RoleSkillWeightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roleSkills")
public class RoleSkillWeightController {
    private static final Logger log= LoggerFactory.getLogger(RoleSkillWeightController.class);

    private final RoleSkillWeightService roleSkillWeightService;

    @PostMapping
    public RoleSkillWeightResponseDto addRoleSkill(
            @Valid @RequestBody RoleSkillWeightRequestDto request) {

        log.info("Received request to save roleSkillWeight: {} ", request);
        return roleSkillWeightService.addRoleSkill(request);
    }

    @GetMapping("/role/{roleId}")
    public List<RoleSkillWeightResponseDto> getSkillsByRoleId(
            @PathVariable Long roleId) {

        log.info("Received request to get roleSkillWeights by roleId: {}", roleId);

        return roleSkillWeightService.getSkillsByRoleId(roleId);
    }


}


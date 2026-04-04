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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roleSkills")
public class RoleSkillWeightController {

    private final RoleSkillWeightService roleSkillWeightService;

    @PostMapping
    public RoleSkillWeightResponseDto addRoleSkill(
            @Valid @RequestBody RoleSkillWeightRequestDto request) {



        return roleSkillWeightService.addRoleSkill(request);
    }

    @GetMapping("/role/{roleId}")
    public List<RoleSkillWeightResponseDto> getSkillsByRoleId(
            @PathVariable Long roleId) {

        return roleSkillWeightService.getSkillsByRoleId(roleId);
    }


}


package com.sharayu.skillgap.controller;


import com.sharayu.skillgap.dto.RoleSkillWeightResponseDto;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.service.RoleSkillWeightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sharayu.skillgap.service.RoleSkillWeightService.*;

@RestController
@RequestMapping("api/roleSkillWeights")
public class RoleSkillWeightController {

    private final RoleSkillWeightService roleSkillWeightService;

    public RoleSkillWeightController(RoleSkillWeightService roleSkillWeightService) {
        this.roleSkillWeightService = roleSkillWeightService;
    }

    @PostMapping
    public RoleSkillWeight addRoleSkill(@RequestParam Long roleId, @RequestParam Long skillId, @RequestParam Integer requiredLevel) {
        return roleSkillWeightService.addRoleSkill(roleId, skillId, requiredLevel);
    }

    @GetMapping("/role/{roleId}")
    public List<RoleSkillWeightResponseDto> getSkillsByRole(@PathVariable Long roleId) {
        return roleSkillWeightService.getSkillsByRole(roleId);
    }

}
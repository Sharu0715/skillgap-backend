package com.sharayu.skillgap.controller;


import com.sharayu.skillgap.dto.RoleRecommendationDto;
import com.sharayu.skillgap.dto.SkillGapAnalysisResponseDto;
import com.sharayu.skillgap.dto.SkillGapDetailDto;
import com.sharayu.skillgap.service.SkillGapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/skillGap")

public class SkillGapController {
    private final SkillGapService skillGapService;

   /* public SkillGapController(SkillGapService skillGapService) {
        this.skillGapService = skillGapService;
    }*/

    @GetMapping("/student/{studentId}/role/{roleId}")
    public SkillGapAnalysisResponseDto analyze(
            @PathVariable Long studentId,
            @PathVariable Long roleId) {

        return skillGapService.analyzeSkillGap(studentId, roleId);
    }

    @GetMapping("/student/{studentId}/all-roles")
    public List<RoleRecommendationDto> analyzeAllRoles(
            @PathVariable Long studentId) {

        return skillGapService.analyzeAllRoles(studentId);
    }

    @GetMapping("/student/{studentId}/best-role")
    public RoleRecommendationDto getBestRole(
            @PathVariable Long studentId) {

        return skillGapService.getBestRole(studentId);
    }

}

package com.sharayu.skillgap.controller;


import com.sharayu.skillgap.dto.RoleRecommendationDto;
import com.sharayu.skillgap.dto.SkillGapAnalysisResponseDto;
import com.sharayu.skillgap.dto.SkillGapDetailDto;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.service.SkillGapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/skillGap")

public class SkillGapController {
    private static final Logger log = LoggerFactory.getLogger(SkillGapController.class);
    private final SkillGapService skillGapService;

   /* public SkillGapController(SkillGapService skillGapService) {
        this.skillGapService = skillGapService;
    }*/

    @GetMapping("/student/{studentId}/role/{roleId}")
    public SkillGapAnalysisResponseDto analyze(
            @PathVariable Student studentId,
            @PathVariable Long roleId) {

        log.info("Received skill gap request for studentId: {}, roleId:{}", studentId, roleId);

        return skillGapService.analyzeSkillGap(studentId, roleId);
    }

    @GetMapping("/student/{studentId}/all-roles")
    public List<RoleRecommendationDto> analyzeAllRoles(
            @PathVariable Long studentId) {

        log.info("Received request  skill to analyze all roles for studentId: {}", studentId);

        return skillGapService.analyzeAllRoles(studentId);
    }

    @GetMapping("/student/{studentId}/best-role")
    public RoleRecommendationDto getBestRole(
            @PathVariable Long studentId) {

        log.info("Received request to  analyze best role for studentId: {}", studentId);

        return skillGapService.getBestRole(studentId);
    }

}

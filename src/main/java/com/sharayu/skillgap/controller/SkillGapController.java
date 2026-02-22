package com.sharayu.skillgap.controller;


import com.sharayu.skillgap.dto.SkillGapAnalysisResponseDto;
import com.sharayu.skillgap.dto.SkillGapDetailDto;
import com.sharayu.skillgap.service.SkillGapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/skillGap")
public class SkillGapController {
    private final SkillGapService skillGapService;

    public SkillGapController(SkillGapService skillGapService) {
        this.skillGapService = skillGapService;
    }

    @GetMapping("/student/{studentId}/role/{roleId}")
    public SkillGapAnalysisResponseDto analyze(
            @PathVariable Long studentId,
            @PathVariable Long roleId) {

        return skillGapService.analyzeSkillGap(studentId, roleId);
    }

}
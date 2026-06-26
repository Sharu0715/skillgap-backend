package com.sharayu.skillgap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SkillGapResult {

    private double matchPercentage;
    private boolean eligible;
    private int totalRequired;
    private int totalAchieved;
    private List<SkillGapDetailDto> details;
}
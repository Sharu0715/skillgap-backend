package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/skills")
public class SkillController {

    private final  SkillService skillService;
   /* public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }*/
    @PostMapping
    public Skill createSkill(@RequestBody Skill skill){
        return skillService.addSkill(skill);
    }

    @GetMapping("/{id}")
    public Skill getSkillById(@PathVariable Long id){
        return skillService.getSkillById(id);
    }

}

package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.service.SkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/skills")
public class SkillController {

    private SkillService skillService;
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
    @PostMapping
    public Skill createSkill(@RequestBody Skill skill){
        return skillService.save(skill);
    }

    @GetMapping
    public List<Skill> getAllSkills(){
        return skillService.getAllSkills();
    }

}

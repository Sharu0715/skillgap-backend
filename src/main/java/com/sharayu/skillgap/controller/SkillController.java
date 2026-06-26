package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/skills")
public class SkillController {
    private static final Logger log = LoggerFactory.getLogger(SkillController.class);

    private final  SkillService skillService;
   /* public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }*/
    @PostMapping
    public Skill createSkill(@RequestBody Skill skill){

        log.info("Received request to create skill : {}", skill);
        return skillService.addSkill(skill);
    }

    @GetMapping("/{id}")
    public Skill getSkillById(@PathVariable Long id){

        log.info("Received request to get Skill by id : {}", id);
        return skillService.getSkillById(id);
    }

}

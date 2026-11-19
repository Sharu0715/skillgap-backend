package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

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


    @GetMapping
    public ResponseEntity<Page<Skill>> getAllSkills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Received request to get skills. Page: {}, Size: {}",
                page, size);

        return ResponseEntity.ok(
                skillService.getAllSkills(
                        PageRequest.of(page, size)
                )
        );
    }


    @PutMapping("/{id}")
    public Skill updateSkill(@PathVariable Long id, @RequestBody Skill skill){

        log.info("Received request to update skill : {}", skill);

        return skillService.updateSkill(id, skill);
    }

    @DeleteMapping("/{id}")
    public Skill deleteSkill(@PathVariable Long id){

        log.info("Received request to delete skill : {}", id);

        skillService.deleteSkill(id);
        return skillService.getSkillById(id);
    }
    @GetMapping("/dropdown")
    public ResponseEntity<List<Skill>> getAllSkillsForDropdown() {
        return ResponseEntity.ok(
                skillService.getAllSkillsForDropdown()
        );
    }
}


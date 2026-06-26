package com.sharayu.skillgap.service;

import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class SkillService {
    private static final Logger log=LoggerFactory.getLogger(SkillService.class);

    private final SkillRepository skillRepository;

    public Skill addSkill(Skill skill) {
        if (skillRepository.existsBySkillName(skill.getSkillName())) {
            throw new DuplicateResourceException("Skill already exits with skill name:  " +skill.getSkillName());
        }
        log.info("Creating  skill with name:{}",skill.getSkillName());

        Skill saved=skillRepository.save(skill);

        log.info("Skill added successfully with id:{}",saved.getId());

        return saved;
    }



    public Skill getSkillById(Long id) {

        log.info("Fetching Skill by id:{}",id);
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " +id));
    }

}

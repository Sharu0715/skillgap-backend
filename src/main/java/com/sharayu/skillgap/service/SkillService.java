package com.sharayu.skillgap.service;

import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public Skill addSkill(Skill skill) {
        if (skillRepository.existsBySkillName(skill.getSkillName())) {
            throw new DuplicateResourceException("Skill already exits with skill name:  " +skill.getSkillName());
        }
        return skillRepository.save(skill);
    }



    public Skill getSkillById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " +id));
    }

}

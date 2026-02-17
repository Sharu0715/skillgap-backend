package com.sharayu.skillgap.service;

import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class SkillService {

    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }
    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

}

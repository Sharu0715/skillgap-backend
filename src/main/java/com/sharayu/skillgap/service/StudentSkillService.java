package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.StudentSkillResponseDto;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.entity.StudentSkill;

import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.SkillRepository;
import com.sharayu.skillgap.repository.StudentRepository;
import com.sharayu.skillgap.repository.StudentSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor

public class StudentSkillService {
    private static final Logger log= LoggerFactory.getLogger(StudentSkillService.class);
    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
     private final StudentSkillRepository studentSkillRepository;

    /* public StudentSkillService(StudentSkillRepository studentSkillRepository) {
        this.studentSkillRepository =  studentSkillRepository;
     }*/
     public StudentSkill addStudentSkill(StudentSkill studentSkill) {


         if (studentSkill == null || studentSkill.getStudent() == null || studentSkill.getSkill() == null) {
             throw new IllegalArgumentException("Student and Skill are required");
         }
         Long studentId = studentSkill.getStudent().getId();
         Long skillId = studentSkill.getSkill().getId();
         Student student = studentRepository.findById(studentId)
                 .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

         log.info("Student Found: {}", studentId);

         Skill skill = skillRepository.findById(skillId)
                 .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));

         log.info("Skill Found: {}", skillId);

         if (studentSkillRepository.existsByStudentAndSkill(studentSkill.getStudent(), studentSkill.getSkill())) {
             throw new DuplicateResourceException(" StudentSkill already exists for StudentId: " +studentId+ " and skillId: " +skillId);
         }

         studentSkill.setStudent(student);
         studentSkill.setSkill(skill);

         log.debug("Saving StudentSkill mapping for studentId:{}, skillId:{}",studentId,skillId);
         log.info("Adding skill for studentId:{}, skillId:{}",skill.getSkillName(), skill.getId());


         return studentSkillRepository.save(studentSkill);
     }
    @Transactional(readOnly = true)
    public List<StudentSkillResponseDto> getSkillsByStudentId(Long studentId) {

         log.info("Fetching  skills for studentId:{}",studentId);

        List<StudentSkill> skills = studentSkillRepository.findByStudentId(studentId);

        log.debug("Skills Found: {}", skills.size());

        if(skills.isEmpty()){
            throw new ResourceNotFoundException("Student skill not found with id: "+studentId);
        }

        log.info("Successfully fetched skills for studentId:{}",studentId);

        return skills.stream()
                .map(ss -> new StudentSkillResponseDto(
                        ss.getSkill().getId(),
                        ss.getSkill().getSkillName(),
                        ss.getCurrentLevel()
                ))
                .toList();
    }


}

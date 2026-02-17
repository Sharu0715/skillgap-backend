package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.StudentSkillResponseDto;
import com.sharayu.skillgap.entity.StudentSkill;
import com.sharayu.skillgap.repository.StudentSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentSkillService {
     private final StudentSkillRepository studentSkillRepository;

     public StudentSkillService(StudentSkillRepository studentSkillRepository) {
        this.studentSkillRepository =  studentSkillRepository;
     }
     public StudentSkill addStudentSkill(StudentSkill studentSkill) {
         return studentSkillRepository.save(studentSkill);
     }

    public List<StudentSkillResponseDto> getSkillsByStudentId(Long studentId) {

        List<StudentSkill> skills = studentSkillRepository.findByStudentId(studentId);

        return skills.stream()
                .map(ss -> new StudentSkillResponseDto(
                        ss.getSkill().getId(),
                        ss.getSkill().getSkillName(),
                        ss.getCurrentLevel()
                ))
                .toList();
    }

}

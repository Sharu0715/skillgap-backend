package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.dto.StudentSkillResponseDto;
import com.sharayu.skillgap.entity.StudentSkill;
import com.sharayu.skillgap.service.StudentSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/studentskills")
public class StudentSkillController {
    private final StudentSkillService studentSkillService;

    public StudentSkillController(StudentSkillService studentSkillService) {
        this.studentSkillService = studentSkillService;

    }
    @PostMapping
             public ResponseEntity<StudentSkill>  addStudentSkill(@RequestBody StudentSkill studentSkill) {
            StudentSkill saved= studentSkillService.addStudentSkill(studentSkill);
            return ResponseEntity.ok(saved);

        }

    @GetMapping("/student/{studentId}")
    public List<StudentSkillResponseDto> getSkills(@PathVariable Long studentId) {
        return studentSkillService.getSkillsByStudentId(studentId);
    }

}



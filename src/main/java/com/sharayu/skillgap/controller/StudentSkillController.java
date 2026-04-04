package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.dto.StudentSkillResponseDto;
import com.sharayu.skillgap.entity.StudentSkill;
import com.sharayu.skillgap.service.StudentSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/student-skills")
public class StudentSkillController {
    private final StudentSkillService studentSkillService;

    /*public StudentSkillController(StudentSkillService studentSkillService) {
        this.studentSkillService = studentSkillService;

    }*/
    @PostMapping
    public ResponseEntity<StudentSkill> addStudentSkill(
            @Valid @RequestBody StudentSkill studentSkill) {

        StudentSkill saved = studentSkillService.addStudentSkill(studentSkill);

        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping("/student/{studentId}")
    public List<StudentSkillResponseDto> getSkills(@PathVariable Long studentId) {
        return studentSkillService.getSkillsByStudentId(studentId);
    }

}



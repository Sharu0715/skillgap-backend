package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.dto.StudentRequestDto;
import com.sharayu.skillgap.dto.StudentResponseDto;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentResponseDto register(@RequestBody StudentRequestDto dto) {
        return studentService.registerStudent(dto);
    }


    @GetMapping("/{id}")
    public StudentResponseDto getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
}

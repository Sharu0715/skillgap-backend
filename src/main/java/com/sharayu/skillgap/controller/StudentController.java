package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.dto.StudentRequestDto;
import com.sharayu.skillgap.dto.StudentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
public class StudentController {
    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

   /* public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }*/

    @PostMapping
    public StudentResponseDto register(@RequestBody StudentRequestDto dto) {

        log.info("Received request to save student : {}", dto);
        return studentService.registerStudent(dto);
    }


    @GetMapping("/{id}")
    public StudentResponseDto getStudent(@PathVariable Long id) {

        log.info("Received request to get student : {}", id);
        return studentService.getStudentById(id);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents(){

        log.info("Received request to get all students");

        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        log.info("Received request to update student : {}", id);
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        log.info("Received request to delete student : {}", id);

        studentService.deleteStudent(id);
        return "Student deleted successfully";
    }
}


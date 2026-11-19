package com.sharayu.skillgap.controller;


import com.sharayu.skillgap.dto.StudentResponseDto;
import com.sharayu.skillgap.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sharayu.skillgap.entity.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;



import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final Logger log =
            LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;



    @GetMapping("/{id}")
    public StudentResponseDto getStudent(
            @PathVariable Long id) {

        log.info("Received request to get student : {}", id);

        return studentService.getStudentById(id);
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponseDto>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Received request to get students. Page: {}, Size: {}",
                page, size);

        return ResponseEntity.ok(
                studentService.getAllStudents(
                        PageRequest.of(page, size)
                )
        );
    }

    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {

        log.info("Received request to update student : {}", id);

        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(
            @PathVariable Long id) {

        log.info("Received request to delete student : {}", id);

        studentService.deleteStudent(id);

        return "Student deleted successfully";
    }

}
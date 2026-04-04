package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.StudentRequestDto;
import com.sharayu.skillgap.dto.StudentResponseDto;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    /*public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }*/

    public StudentResponseDto registerStudent(StudentRequestDto dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());

        Student saved = studentRepository.save(student);

        return new StudentResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }

    public StudentResponseDto getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student Not found for this id: "+id));

        return new StudentResponseDto(
                student.getId(),
                student.getName(),
                student.getEmail()
        );
    }

}
package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.StudentRequestDto;
import com.sharayu.skillgap.dto.StudentResponseDto;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private static final Logger log=LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    /*public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }*/

    public StudentResponseDto registerStudent(StudentRequestDto dto) {

        log.info("Starting student register request Name: {}, Email:{}",
                dto.getName(),dto.getEmail());
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());

        Student saved = studentRepository.save(student);

        log.info("Student registered Successfully with id:{}, Name:{},Email:{}",saved.getId(), saved.getName(),saved.getEmail());

        return new StudentResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }

    public StudentResponseDto getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student Not found for this id: "+id));

        log.info("Student found successfully with id:{}, Name:{},Email:{}",student.getId(),student.getName(),student.getEmail());

        return new StudentResponseDto(
                student.getId(),
                student.getName(),
                student.getEmail()
        );
    }

    public List<Student> getAllStudents() {

        log.info("Starting all students request");

        return studentRepository.findAll();
    }

    public Student updateStudent(Long id, Student updatedStudent) {

        Student existing= studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student Not found for this id: "+id));

        log.info("Student found with id: {},updatedStudent:{}",existing.getName(),existing.getEmail());

        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());

        return studentRepository.save(existing);
    }


    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student Not found for this id: "+id));

        log.info("student found successfully with id:{}",student.getId());

        studentRepository.delete(student);
    }

}
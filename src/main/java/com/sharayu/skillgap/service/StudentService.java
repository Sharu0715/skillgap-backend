package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.StudentRequestDto;
import com.sharayu.skillgap.dto.StudentResponseDto;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sharayu.skillgap.dto.RegisterRequestDto;
import com.sharayu.skillgap.service.EmailOtpService;


import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class StudentService {

    private static final Logger log =
            LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailOtpService emailOtpService;

    public StudentResponseDto registerStudent(StudentRequestDto dto) {

        log.info("Starting student registration for email : {}", dto.getEmail());

        if (studentRepository.findByEmail(dto.getEmail()).isPresent()) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );

        }

        Student student = new Student();

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        Student saved = studentRepository.save(student);

        log.info("Student registered successfully with id : {}", saved.getId());

        return new StudentResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );

    }


    public StudentResponseDto registerVerifiedStudent(RegisterRequestDto dto) {

        String normalizedEmail = dto.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        log.info("Starting verified student registration");

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException(
                    "Password and confirm password do not match."
            );
        }

        if (studentRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }

        if (!emailOtpService.isEmailVerified(normalizedEmail)) {
            throw new IllegalStateException(
                    "Please verify your email before registration."
            );
        }

        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(normalizedEmail);
        student.setPassword(passwordEncoder.encode(dto.getPassword()));

        Student saved = studentRepository.save(student);

        emailOtpService.removeOtp(normalizedEmail);

        log.info("Student registered successfully with id: {}", saved.getId());

        return new StudentResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }


    public StudentResponseDto getStudentById(Long id) {

        validateStudentAccess(id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id : " + id
                        )
                );

        return new StudentResponseDto(
                student.getId(),
                student.getName(),
                student.getEmail()
        );

    }

    public Page<StudentResponseDto> getAllStudents(Pageable pageable) {

        log.info("Fetching students with pagination");

        return studentRepository.findAll(pageable)
                .map(student -> new StudentResponseDto(
                        student.getId(),
                        student.getName(),
                        student.getEmail()
                ));
    }

    public Student updateStudent(Long id, Student updatedStudent) {

        validateStudentAccess(id);

        Student existing = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id : " + id
                        )
                );

        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());

        return studentRepository.save(existing);

    }

    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id : " + id
                        )
                );

        studentRepository.delete(student);

    }

    private void validateStudentAccess(Long studentId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {

            return;

        }

        String email = authentication.getName();

        Student loggedInStudent =
                studentRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Logged in student not found"
                                )
                        );

        if (!loggedInStudent.getId().equals(studentId)) {

            throw new AccessDeniedException(
                    "You can access only your own profile."
            );

        }

    }

}
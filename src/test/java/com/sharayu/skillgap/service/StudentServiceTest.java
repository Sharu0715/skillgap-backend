
package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.StudentRequestDto;
import com.sharayu.skillgap.dto.StudentResponseDto;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sharayu.skillgap.exception.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    @Test
    void registerStudent_shouldThrowException_whenEmailAlreadyExists() {

        // Arrange
        StudentRequestDto dto = new StudentRequestDto();
        dto.setName("Sharayu");
        dto.setEmail("sharayu@gmail.com");
        dto.setPassword("Test@123");

        Student existingStudent = new Student();
        existingStudent.setEmail("sharayu@gmail.com");

        when(studentRepository.findByEmail("sharayu@gmail.com"))
                .thenReturn(Optional.of(existingStudent));

        // Act & Assert
        assertThrows(
                DuplicateResourceException.class,
                () -> studentService.registerStudent(dto)
        );

        // Verify student is not saved
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void registerStudent_shouldRegisterSuccessfully_andEncodePassword() {

        StudentRequestDto dto = new StudentRequestDto();
        dto.setName("Sharayu");
        dto.setEmail("sharayu@gmail.com");
        dto.setPassword("Test@123");

        when(studentRepository.findByEmail("sharayu@gmail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("Test@123"))
                .thenReturn("encodedPassword");

        when(studentRepository.save(any(Student.class)))
                .thenAnswer(invocation -> {
                    Student student = invocation.getArgument(0);
                    student.setId(1L);
                    return student;
                });

        StudentResponseDto response = studentService.registerStudent(dto);

        assertEquals(1L, response.getId());
        assertEquals("Sharayu", response.getName());
        assertEquals("sharayu@gmail.com", response.getEmail());

        verify(passwordEncoder).encode("Test@123");
        verify(studentRepository).save(any(Student.class));
    }
    @Test
    void getStudentById_shouldThrowException_whenStudentNotFound() {

        Long studentId = 1L;

        org.springframework.security.core.Authentication authentication =
                org.mockito.Mockito.mock(
                        org.springframework.security.core.Authentication.class
                );

        org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        org.mockito.Mockito.doReturn(
                java.util.List.of(
                        new org.springframework.security.core.authority
                                .SimpleGrantedAuthority("ROLE_ADMIN")
                )
        ).when(authentication).getAuthorities();
        when(studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        assertThrows(
                com.sharayu.skillgap.exception.ResourceNotFoundException.class,
                () -> studentService.getStudentById(studentId)
        );

        verify(studentRepository).findById(studentId);

        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }
    @Test
    void getStudentById_shouldThrowException_whenAccessingOtherStudentProfile() {

        Long loggedInStudentId = 1L;
        Long requestedStudentId = 2L;

        org.springframework.security.core.Authentication authentication =
                org.mockito.Mockito.mock(
                        org.springframework.security.core.Authentication.class
                );

        org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        org.mockito.Mockito.doReturn(
                java.util.List.of(
                        new org.springframework.security.core.authority
                                .SimpleGrantedAuthority("ROLE_STUDENT")
                )
        ).when(authentication).getAuthorities();

        org.mockito.Mockito.when(authentication.getName())
                .thenReturn("student1@gmail.com");

        Student loggedInStudent = new Student();
        loggedInStudent.setId(loggedInStudentId);
        loggedInStudent.setEmail("student1@gmail.com");

        org.mockito.Mockito.when(
                studentRepository.findByEmail("student1@gmail.com")
        ).thenReturn(Optional.of(loggedInStudent));

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.security.access.AccessDeniedException.class,
                () -> studentService.getStudentById(requestedStudentId)
        );

        org.springframework.security.core.context.SecurityContextHolder
                .clearContext();
    }
    @Test
    void deleteStudent_shouldThrowException_whenStudentNotFound() {

        Long studentId = 1L;

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> studentService.deleteStudent(studentId)
        );

        verify(studentRepository).findById(studentId);
        verify(studentRepository, never()).delete(any(Student.class));
    }
    @Test
    void deleteStudent_shouldDeleteStudentSuccessfully() {

        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);
        student.setName("Sharayu");
        student.setEmail("sharayu@gmail.com");

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        studentService.deleteStudent(studentId);

        verify(studentRepository).findById(studentId);
        verify(studentRepository).delete(student);
    }
    @Test
    void getStudentById_shouldReturnStudent_whenLoggedInStudentAccessesOwnProfile() {

        Long studentId = 1L;
        String email = "sharayu@gmail.com";

        org.springframework.security.core.Authentication authentication =
                org.mockito.Mockito.mock(
                        org.springframework.security.core.Authentication.class
                );

        org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        org.mockito.Mockito.doReturn(
                java.util.List.of(
                        new org.springframework.security.core.authority
                                .SimpleGrantedAuthority("ROLE_STUDENT")
                )
        ).when(authentication).getAuthorities();

        when(authentication.getName()).thenReturn(email);

        Student student = new Student();
        student.setId(studentId);
        student.setName("Sharayu");
        student.setEmail(email);

        when(studentRepository.findByEmail(email))
                .thenReturn(Optional.of(student));

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        try {
            StudentResponseDto response =
                    studentService.getStudentById(studentId);

            assertEquals(studentId, response.getId());
            assertEquals("Sharayu", response.getName());
            assertEquals(email, response.getEmail());

            verify(studentRepository).findByEmail(email);
            verify(studentRepository).findById(studentId);

        } finally {
            org.springframework.security.core.context.SecurityContextHolder
                    .clearContext();
        }
    }
    @Test
    void updateStudent_shouldThrowException_whenStudentNotFound() {

        Long studentId = 1L;

        Student updatedStudent = new Student();
        updatedStudent.setName("Sharayu Updated");
        updatedStudent.setEmail("updated@gmail.com");

        org.springframework.security.core.Authentication authentication =
                org.mockito.Mockito.mock(
                        org.springframework.security.core.Authentication.class
                );

        org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        org.mockito.Mockito.doReturn(
                java.util.List.of(
                        new org.springframework.security.core.authority
                                .SimpleGrantedAuthority("ROLE_ADMIN")
                )
        ).when(authentication).getAuthorities();

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        try {
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> studentService.updateStudent(studentId, updatedStudent)
            );

            verify(studentRepository, never()).save(any(Student.class));

        } finally {
            org.springframework.security.core.context.SecurityContextHolder
                    .clearContext();
        }
    }
    @Test
    void updateStudent_shouldUpdateSuccessfully_whenStudentExists() {

        Long studentId = 1L;

        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setName("Old Name");
        existingStudent.setEmail("old@gmail.com");

        Student updatedStudent = new Student();
        updatedStudent.setName("New Name");
        updatedStudent.setEmail("new@gmail.com");

        org.springframework.security.core.Authentication authentication =
                org.mockito.Mockito.mock(
                        org.springframework.security.core.Authentication.class
                );

        org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        org.mockito.Mockito.doReturn(
                java.util.List.of(
                        new org.springframework.security.core.authority
                                .SimpleGrantedAuthority("ROLE_ADMIN")
                )
        ).when(authentication).getAuthorities();

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        try {
            Student result =
                    studentService.updateStudent(studentId, updatedStudent);

            assertEquals("New Name", result.getName());
            assertEquals("new@gmail.com", result.getEmail());
            assertEquals(studentId, result.getId());

            verify(studentRepository).save(existingStudent);

        } finally {
            org.springframework.security.core.context.SecurityContextHolder
                    .clearContext();
        }
    }
    @Test
    void getAllStudents_shouldReturnPaginatedStudentResponse() {

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Sharayu");
        student1.setEmail("sharayu@gmail.com");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Priya");
        student2.setEmail("priya@gmail.com");

        org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest.of(0, 10);

        org.springframework.data.domain.Page<Student> studentPage =
                new org.springframework.data.domain.PageImpl<>(
                        java.util.List.of(student1, student2),
                        pageable,
                        2
                );

        when(studentRepository.findAll(pageable))
                .thenReturn(studentPage);

        org.springframework.data.domain.Page<StudentResponseDto> result =
                studentService.getAllStudents(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Sharayu", result.getContent().get(0).getName());
        assertEquals("Priya", result.getContent().get(1).getName());

        verify(studentRepository).findAll(pageable);
    }
    @Test
    void getStudentById_shouldThrowException_whenLoggedInStudentNotFound() {

        Long studentId = 1L;
        String email = "unknown@gmail.com";

        org.springframework.security.core.Authentication authentication =
                org.mockito.Mockito.mock(
                        org.springframework.security.core.Authentication.class
                );

        org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        org.mockito.Mockito.doReturn(
                java.util.List.of(
                        new org.springframework.security.core.authority
                                .SimpleGrantedAuthority("ROLE_STUDENT")
                )
        ).when(authentication).getAuthorities();

        when(authentication.getName()).thenReturn(email);

        when(studentRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        try {
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> studentService.getStudentById(studentId)
            );

            verify(studentRepository).findByEmail(email);
            verify(studentRepository, never()).findById(studentId);

        } finally {
            org.springframework.security.core.context.SecurityContextHolder
                    .clearContext();
        }
    }
}

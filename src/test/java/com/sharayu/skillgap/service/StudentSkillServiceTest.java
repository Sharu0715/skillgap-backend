
package com.sharayu.skillgap.service;

import com.sharayu.skillgap.entity.JobRole;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.entity.StudentSkill;
import com.sharayu.skillgap.dto.StudentSkillResponseDto;
import com.sharayu.skillgap.exception.DuplicateResourceException;
import com.sharayu.skillgap.exception.ResourceNotFoundException;
import com.sharayu.skillgap.repository.SkillRepository;
import com.sharayu.skillgap.repository.StudentRepository;
import com.sharayu.skillgap.repository.StudentSkillRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentSkillServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private StudentSkillRepository studentSkillRepository;

    @InjectMocks
    private StudentSkillService studentSkillService;


    @Test
    void addStudentSkill_shouldThrowException_whenStudentNotFound() {

        Student student = new Student();
        student.setId(99L);

        Skill skill = new Skill();
        skill.setId(1L);

        StudentSkill studentSkill = new StudentSkill();
        studentSkill.setStudent(student);
        studentSkill.setSkill(skill);
        studentSkill.setCurrentLevel(80);

        Student loggedInStudent = new Student();
        loggedInStudent.setId(99L);
        loggedInStudent.setEmail("student@gmail.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(loggedInStudent));

            when(studentRepository.findById(99L))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> studentSkillService.addStudentSkill(studentSkill));

            verify(studentSkillRepository, never()).save(any(StudentSkill.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void addStudentSkill_shouldThrowException_whenSkillNotFound() {

        Student student = new Student();
        student.setId(1L);

        Skill skill = new Skill();
        skill.setId(99L);

        StudentSkill studentSkill = new StudentSkill();
        studentSkill.setStudent(student);
        studentSkill.setSkill(skill);
        studentSkill.setCurrentLevel(80);

        Student loggedInStudent = new Student();
        loggedInStudent.setId(1L);
        loggedInStudent.setEmail("student@gmail.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(loggedInStudent));

            when(studentRepository.findById(1L))
                    .thenReturn(Optional.of(student));

            when(skillRepository.findById(99L))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> studentSkillService.addStudentSkill(studentSkill));

            verify(studentSkillRepository, never()).save(any(StudentSkill.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void addStudentSkill_shouldThrowException_whenMappingAlreadyExists() {

        Student student = new Student();
        student.setId(1L);

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        StudentSkill studentSkill = new StudentSkill();
        studentSkill.setStudent(student);
        studentSkill.setSkill(skill);
        studentSkill.setCurrentLevel(80);

        Student loggedInStudent = new Student();
        loggedInStudent.setId(1L);
        loggedInStudent.setEmail("student@gmail.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(loggedInStudent));

            when(studentRepository.findById(1L))
                    .thenReturn(Optional.of(student));

            when(skillRepository.findById(1L))
                    .thenReturn(Optional.of(skill));

            when(studentSkillRepository.existsByStudentAndSkill(student, skill))
                    .thenReturn(true);

            assertThrows(DuplicateResourceException.class,
                    () -> studentSkillService.addStudentSkill(studentSkill));

            verify(studentSkillRepository, never()).save(any(StudentSkill.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void addStudentSkill_shouldSaveSuccessfully_whenMappingDoesNotExist() {

        Student student = new Student();
        student.setId(1L);
        student.setEmail("student@gmail.com");

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        StudentSkill studentSkill = new StudentSkill();
        studentSkill.setStudent(student);
        studentSkill.setSkill(skill);
        studentSkill.setCurrentLevel(80);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(student));

            when(studentRepository.findById(1L))
                    .thenReturn(Optional.of(student));

            when(skillRepository.findById(1L))
                    .thenReturn(Optional.of(skill));

            when(studentSkillRepository.existsByStudentAndSkill(student, skill))
                    .thenReturn(false);

            when(studentSkillRepository.save(any(StudentSkill.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            StudentSkill result = studentSkillService.addStudentSkill(studentSkill);

            assertNotNull(result);
            assertEquals(1L, result.getStudent().getId());
            assertEquals(1L, result.getSkill().getId());
            assertEquals(80, result.getCurrentLevel());

            verify(studentSkillRepository).save(studentSkill);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void getSkillsByStudentId_shouldThrowException_whenNoSkillsFound() {

        Student loggedInStudent = new Student();
        loggedInStudent.setId(1L);
        loggedInStudent.setEmail("student@gmail.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(loggedInStudent));

            when(studentSkillRepository.findByStudentId(1L))
                    .thenReturn(List.of());

            assertThrows(ResourceNotFoundException.class,
                    () -> studentSkillService.getSkillsByStudentId(1L));

            verify(studentSkillRepository).findByStudentId(1L);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void getSkillsByStudentId_shouldReturnMappedSkills() {

        Student loggedInStudent = new Student();
        loggedInStudent.setId(1L);
        loggedInStudent.setEmail("student@gmail.com");

        Skill javaSkill = new Skill();
        javaSkill.setId(1L);
        javaSkill.setSkillName("Java");

        Skill springSkill = new Skill();
        springSkill.setId(2L);
        springSkill.setSkillName("Spring Boot");

        StudentSkill javaMapping = new StudentSkill();
        javaMapping.setStudent(loggedInStudent);
        javaMapping.setSkill(javaSkill);
        javaMapping.setCurrentLevel(80);

        StudentSkill springMapping = new StudentSkill();
        springMapping.setStudent(loggedInStudent);
        springMapping.setSkill(springSkill);
        springMapping.setCurrentLevel(90);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(loggedInStudent));

            when(studentSkillRepository.findByStudentId(1L))
                    .thenReturn(List.of(javaMapping, springMapping));

            List<StudentSkillResponseDto> result =
                    studentSkillService.getSkillsByStudentId(1L);

            assertEquals(2, result.size());

            assertEquals(1L, result.get(0).getSkillId());
            assertEquals("Java", result.get(0).getSkillName());
            assertEquals(80, result.get(0).getCurrentLevel());

            assertEquals(2L, result.get(1).getSkillId());
            assertEquals("Spring Boot", result.get(1).getSkillName());
            assertEquals(90, result.get(1).getCurrentLevel());

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void updateStudentSkill_shouldThrowException_whenMappingNotFound() {

        Student loggedInStudent = new Student();
        loggedInStudent.setId(1L);
        loggedInStudent.setEmail("student@gmail.com");

        StudentSkill updatedSkill = new StudentSkill();
        updatedSkill.setCurrentLevel(90);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(loggedInStudent));

            when(studentSkillRepository.findByStudentIdAndSkillId(1L, 99L))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> studentSkillService.updateStudentSkill(
                            1L, 99L, updatedSkill));

            verify(studentSkillRepository, never()).save(any(StudentSkill.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void updateStudentSkill_shouldUpdateCurrentLevel_whenMappingExists() {

        Student loggedInStudent = new Student();
        loggedInStudent.setId(1L);
        loggedInStudent.setEmail("student@gmail.com");

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        StudentSkill existingMapping = new StudentSkill();
        existingMapping.setStudent(loggedInStudent);
        existingMapping.setSkill(skill);
        existingMapping.setCurrentLevel(70);

        StudentSkill updateRequest = new StudentSkill();
        updateRequest.setCurrentLevel(90);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(loggedInStudent));

            when(studentSkillRepository.findByStudentIdAndSkillId(1L, 1L))
                    .thenReturn(Optional.of(existingMapping));

            when(studentSkillRepository.save(existingMapping))
                    .thenReturn(existingMapping);

            StudentSkill result = studentSkillService.updateStudentSkill(
                    1L, 1L, updateRequest);

            assertEquals(90, result.getCurrentLevel());

            verify(studentSkillRepository).save(existingMapping);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void deleteStudentSkill_shouldThrowException_whenMappingNotFound() {

        Student loggedInStudent = new Student();
        loggedInStudent.setId(1L);
        loggedInStudent.setEmail("student@gmail.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(loggedInStudent));

            when(studentSkillRepository.findByStudentIdAndSkillId(1L, 99L))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> studentSkillService.deleteStudentSkill(1L, 99L));

            verify(studentSkillRepository, never())
                    .delete(any(StudentSkill.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void deleteStudentSkill_shouldDeleteSuccessfully_whenMappingExists() {

        Student loggedInStudent = new Student();
        loggedInStudent.setId(1L);
        loggedInStudent.setEmail("student@gmail.com");

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        StudentSkill existingMapping = new StudentSkill();
        existingMapping.setStudent(loggedInStudent);
        existingMapping.setSkill(skill);
        existingMapping.setCurrentLevel(80);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "student@gmail.com",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                )
        );

        try {
            when(studentRepository.findByEmail("student@gmail.com"))
                    .thenReturn(Optional.of(loggedInStudent));

            when(studentSkillRepository.findByStudentIdAndSkillId(1L, 1L))
                    .thenReturn(Optional.of(existingMapping));

            studentSkillService.deleteStudentSkill(1L, 1L);

            verify(studentSkillRepository)
                    .delete(existingMapping);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

}

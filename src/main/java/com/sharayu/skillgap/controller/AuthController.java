
package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.dto.LoginRequestDto;
import com.sharayu.skillgap.dto.LoginResponseDto;
import com.sharayu.skillgap.entity.Admin;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.repository.AdminRepository;
import com.sharayu.skillgap.repository.StudentRepository;
import com.sharayu.skillgap.security.CustomUserDetailsService;
import com.sharayu.skillgap.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.sharayu.skillgap.dto.RegisterRequestDto;
import com.sharayu.skillgap.dto.StudentResponseDto;
import com.sharayu.skillgap.service.StudentService;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final StudentService studentService;

    @PostMapping("/login")
    public LoginResponseDto login(
            @Valid @RequestBody LoginRequestDto request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalizedEmail,
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        normalizedEmail
                );

        String token = jwtUtil.generateToken(userDetails);

        // Check whether the user is an Admin
        var adminOptional =
                adminRepository.findByEmail( normalizedEmail);

        if (adminOptional.isPresent()) {

            Admin admin = adminOptional.get();

            return new LoginResponseDto(
                    token,
                    null,
                    admin.getId(),
                    admin.getName(),
                    "ADMIN"
            );
        }

        // Otherwise, return Student details
        Student student = studentRepository
                .findByEmail( normalizedEmail)
                .orElseThrow(() ->
                        new RuntimeException("Student not found")
                );

        return new LoginResponseDto(
                token,
                student.getId(),
                null,
                student.getName(),
                "STUDENT"
        );
    }

    @PostMapping("/register")
    public StudentResponseDto register(
            @Valid @RequestBody RegisterRequestDto request) {

        return studentService.registerVerifiedStudent(request);
    }

}
package com.sharayu.skillgap.security;

import com.sharayu.skillgap.entity.Admin;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.repository.AdminRepository;
import com.sharayu.skillgap.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        Optional<Admin> admin =
                adminRepository.findByEmail(normalizedEmail);

        if (admin.isPresent()) {

            return new User(
                    admin.get().getEmail(),
                    admin.get().getPassword(),
                    Collections.singletonList(
                            new SimpleGrantedAuthority(admin.get().getRole())
                    )
            );
        }

        Student student = studentRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found")
                );

        return new User(
                student.getEmail(),
                student.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_STUDENT")
                )
        );
    }
}
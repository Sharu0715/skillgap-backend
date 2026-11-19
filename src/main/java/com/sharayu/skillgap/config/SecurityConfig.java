package com.sharayu.skillgap.config;

import com.sharayu.skillgap.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;




@Configuration
@EnableWebSecurity
@RequiredArgsConstructor


public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private static final Logger log =
            LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {

        org.springframework.web.cors.CorsConfiguration configuration =
                new org.springframework.web.cors.CorsConfiguration();

        configuration.setAllowedOrigins(
                java.util.List.of("http://localhost:5173", "http://localhost:5174")
        );

        configuration.setAllowedMethods(
                java.util.List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE"
                )
        );

        configuration.setAllowedHeaders(
                java.util.List.of("*")
        );

        configuration.setAllowCredentials(true);


        org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Unauthorized request: {}", request.getRequestURI());

                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(
                                    "{\"message\":\"Authentication required. Please log in.\"}"
                            );
                        })

                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.warn("Access denied for request: {}", request.getRequestURI());

                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(
                                    "{\"message\":\"You do not have permission to access this resource.\"}"
                            );
                        })
                )


                .sessionManagement(session ->
                   session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/students/{id}")
                        .hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.PUT,"/api/students/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/students/**").hasRole("ADMIN")
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/skills/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers("/api/skills/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/jobRoles/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers("/api/jobRoles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/roleSkills/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers("/api/roleSkills/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/student-skills/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.POST,"/api/student-skills/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.PUT,"/api/student-skills/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.DELETE,"/api/student-skills/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET,"/api/skillGap/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/students").denyAll()
                        .anyRequest().authenticated())


                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }




}

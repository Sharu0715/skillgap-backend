package com.sharayu.skillgap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sharayu.skillgap.entity.Student;
import java.util.Optional;



public interface StudentRepository extends JpaRepository<Student, Long> {


    Optional<Student> findByEmail(String email);
}


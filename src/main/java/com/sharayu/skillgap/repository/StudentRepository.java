package com.sharayu.skillgap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sharayu.skillgap.entity.Student;
public interface StudentRepository extends JpaRepository<Student, Long> {


}


package com.sharayu.skillgap.repository;

import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.entity.Student;
import com.sharayu.skillgap.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.util.List;

public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {
    @Query("SELECT ss FROM StudentSkill ss JOIN FETCH ss.skill WHERE ss.student.id = :studentId")
    List<StudentSkill> findByStudentId(@Param("studentId") Long studentId);
     boolean existsByStudentAndSkill(Student student, Skill skillId );


    Optional<StudentSkill> findByStudentIdAndSkillId(Long studentId, Long skillId);
}

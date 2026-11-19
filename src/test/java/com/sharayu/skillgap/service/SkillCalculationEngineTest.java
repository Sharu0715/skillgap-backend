
package com.sharayu.skillgap.service;

import com.sharayu.skillgap.dto.SkillGapResult;
import com.sharayu.skillgap.entity.Skill;
import com.sharayu.skillgap.entity.RoleSkillWeight;
import com.sharayu.skillgap.entity.StudentSkill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkillCalculationEngineTest {

    private SkillCalculationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new SkillCalculationEngine();

        ReflectionTestUtils.setField(
                engine, "threshold", 70.0
        );
    }






    @Test
    void calculate_shouldThrowException_whenStudentHasDuplicateSkillIds() {

        Skill java = new Skill();
        java.setId(1L);
        java.setSkillName("Java");

        StudentSkill firstJava = new StudentSkill();
        firstJava.setSkill(java);
        firstJava.setCurrentLevel(50);

        StudentSkill secondJava = new StudentSkill();
        secondJava.setSkill(java);
        secondJava.setCurrentLevel(70);

        assertThrows(
                IllegalStateException.class,
                () -> engine.calculate(
                        List.of(),
                        List.of(firstJava, secondJava)
                )
        );
    }

}

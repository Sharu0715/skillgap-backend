
package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.entity.StudentSkill;
import com.sharayu.skillgap.exception.GlobalExceptionHandler;
import com.sharayu.skillgap.security.CustomUserDetailsService;
import com.sharayu.skillgap.security.JwtUtil;
import com.sharayu.skillgap.service.StudentSkillService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentSkillController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class StudentSkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentSkillService studentSkillService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldRejectCurrentLevelAbove100() throws Exception {

        mockMvc.perform(post("/api/student-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "currentLevel": 101
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(studentSkillService, never())
                .addStudentSkill(any(StudentSkill.class));
    }
    @Test
    void shouldRejectCurrentLevelBelowZero() throws Exception {

        mockMvc.perform(post("/api/student-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "currentLevel": -1
                            }
                            """))
                .andExpect(status().isBadRequest());

        verify(studentSkillService, never())
                .addStudentSkill(any(StudentSkill.class));
    }
    @Test
    void shouldAcceptCurrentLevelZero() throws Exception {

        mockMvc.perform(post("/api/student-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "currentLevel": 0
                            }
                            """))
                .andExpect(status().isCreated());

        verify(studentSkillService).addStudentSkill(any(StudentSkill.class));
    }
    @Test
    void shouldAcceptCurrentLevel100() throws Exception {

        mockMvc.perform(post("/api/student-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "currentLevel": 100
                            }
                            """))
                .andExpect(status().isCreated());

        verify(studentSkillService)
                .addStudentSkill(any(StudentSkill.class));
    }
    @Test
    void shouldCallServiceWhenValidRequestIsReceived() throws Exception {

        mockMvc.perform(post("/api/student-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "currentLevel": 75
                            }
                            """))
                .andExpect(status().isCreated());

        verify(studentSkillService)
                .addStudentSkill(any(StudentSkill.class));
    }
    @Test
    void shouldReturnStudentSkills() throws Exception {

        org.mockito.Mockito.when(
                studentSkillService.getSkillsByStudentId(1L)
        ).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/student-skills/student/1")
                )
                .andExpect(status().isOk());

        verify(studentSkillService).getSkillsByStudentId(1L);
    }
    @Test
    void shouldReturnStudentSkillsResponse() throws Exception {

        org.mockito.Mockito.when(
                studentSkillService.getSkillsByStudentId(1L)
        ).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/student-skills/student/1")
                )
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .content().json("[]"));

        verify(studentSkillService).getSkillsByStudentId(1L);
    }
    @Test
    void shouldDeleteStudentSkillSuccessfully() throws Exception {

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/student-skills/student/1/skill/2")
                )
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .content().string("StudentSkill deleted successfully"));

        verify(studentSkillService).deleteStudentSkill(1L, 2L);
    }
    @Test
    void shouldUpdateStudentSkillSuccessfully() throws Exception {

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/student-skills/student/1/skill/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "currentLevel": 75
                                    }
                                    """)
                )
                .andExpect(status().isOk());

        verify(studentSkillService)
                .updateStudentSkill(
                        org.mockito.ArgumentMatchers.eq(1L),
                        org.mockito.ArgumentMatchers.eq(2L),
                        any(StudentSkill.class)
                );
    }
    @Test
    void shouldRejectUpdateWhenCurrentLevelAbove100() throws Exception {

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/student-skills/student/1/skill/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "currentLevel": 101
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verify(studentSkillService, never())
                .updateStudentSkill(
                        org.mockito.ArgumentMatchers.eq(1L),
                        org.mockito.ArgumentMatchers.eq(2L),
                        any(StudentSkill.class)
                );
    }
    @Test
    void shouldRejectUpdateWhenCurrentLevelBelowZero() throws Exception {

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/student-skills/student/1/skill/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "currentLevel": -1
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verify(studentSkillService, never())
                .updateStudentSkill(
                        org.mockito.ArgumentMatchers.eq(1L),
                        org.mockito.ArgumentMatchers.eq(2L),
                        any(StudentSkill.class)
                );
    }
    @Test
    void shouldRejectEmptyRequestBody() throws Exception {

        mockMvc.perform(
                        post("/api/student-skills")
                )
                .andExpect(status().isBadRequest());

        verify(studentSkillService, never())
                .addStudentSkill(any(StudentSkill.class));
    }
    @Test
    void shouldRejectInvalidJson() throws Exception {

        mockMvc.perform(post("/api/student-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verify(studentSkillService, never())
                .addStudentSkill(any(StudentSkill.class));
    }

}

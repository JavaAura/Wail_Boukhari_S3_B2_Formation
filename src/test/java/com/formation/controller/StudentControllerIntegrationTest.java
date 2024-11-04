package com.formation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.entity.Student;
import com.formation.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private Student testStudent;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testStudent = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .level("Intermediate")
                .build();
                
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createStudent_WithValidData_ShouldReturnCreated() throws Exception {
        when(studentService.save(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testStudent.getId()));
    }

    @Test
    void getAllStudents_ShouldReturnPageOfStudents() throws Exception {
        Page<Student> page = new PageImpl<>(Arrays.asList(testStudent));
        when(studentService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testStudent.getId()));
    }

    @Test
    void findByLevel_ShouldReturnStudentsWithLevel() throws Exception {
        Page<Student> page = new PageImpl<>(Arrays.asList(testStudent));
        when(studentService.findByLevel(any(String.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/students/level/{level}", "Intermediate")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].level").value("Intermediate"));
    }

    @Test
    void findByClassRoomId_ShouldReturnStudentsInClassRoom() throws Exception {
        Page<Student> page = new PageImpl<>(Arrays.asList(testStudent));
        when(studentService.findByClassRoomId(any(Long.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/students/classroom/{classRoomId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testStudent.getId()));
    }

    @Test
    void findByCourseId_ShouldReturnStudentsInCourse() throws Exception {
        Page<Student> page = new PageImpl<>(Arrays.asList(testStudent));
        when(studentService.findByCourseId(any(Long.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/students/course/{courseId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testStudent.getId()));
    }

    @Test
    void searchStudents_ShouldReturnMatchingStudents() throws Exception {
        Page<Student> page = new PageImpl<>(Arrays.asList(testStudent));
        when(studentService.search(any(String.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/students/search")
                .param("keyword", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("John"));
    }
}
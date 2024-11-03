package com.formation.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.entity.Student;
import com.formation.service.StudentService;

@WebMvcTest(StudentController.class)
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .level("Intermediate")
                .course(null)
                .classRoom(null)
                .build();
    }

    @Test
    void createStudent_WithValidData_ShouldReturnCreated() throws Exception {
        when(studentService.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    void getStudentById_WhenExists_ShouldReturnStudent() throws Exception {
        when(studentService.findById(1L)).thenReturn(student);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("john.doe@test.com")));
    }

    @Test
    void getAllStudents_ShouldReturnPageOfStudents() throws Exception {
        when(studentService.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(student)));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")));
    }

    @Test
    void findByLevel_ShouldReturnStudentsWithLevel() throws Exception {
        when(studentService.findByLevel(any(String.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(student)));

        mockMvc.perform(get("/api/students/level")
                .param("level", "Intermediate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].level", is("Intermediate")));
    }

    @Test
    void findByClassRoomId_ShouldReturnStudentsInClassRoom() throws Exception {
        when(studentService.findByClassRoomId(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(student)));

        mockMvc.perform(get("/api/students/classroom/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")));
    }

    @Test
    void findByCourseId_ShouldReturnStudentsInCourse() throws Exception {
        when(studentService.findByCourseId(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(student)));

        mockMvc.perform(get("/api/students/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")));
    }

    @Test
    void searchStudents_ShouldReturnMatchingStudents() throws Exception {
        when(studentService.search(any(String.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(student)));

        mockMvc.perform(get("/api/students/search")
                .param("keyword", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")));
    }

    @Test
    void deleteStudent_WhenNotEnrolled_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}

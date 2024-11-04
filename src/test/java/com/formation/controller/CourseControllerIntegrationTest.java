package com.formation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.entity.Course;
import com.formation.entity.enums.CourseStatus;
import com.formation.service.CourseService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    private Course testCourse;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testCourse = Course.builder()
                .id(1L)
                .title("Java Programming")
                .level("Intermediate")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(30))
                .minCapacity(5)
                .maxCapacity(20)
                .status(CourseStatus.PLANNED)
                .students(new HashSet<>())
                .build();
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createCourse_WithValidData_ShouldReturnCreated() throws Exception {
        when(courseService.save(any(Course.class))).thenReturn(testCourse);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCourse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testCourse.getId()))
                .andExpect(jsonPath("$.title").value(testCourse.getTitle()));
    }

    @Test
    void getAllCourses_ShouldReturnPageOfCourses() throws Exception {
        Page<Course> page = new PageImpl<>(Arrays.asList(testCourse));
        when(courseService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/courses")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testCourse.getId()));
    }

    @Test
    void getCourseById_WhenExists_ShouldReturnCourse() throws Exception {
        when(courseService.findById(1L)).thenReturn(testCourse);

        mockMvc.perform(get("/api/courses/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCourse.getId()));
    }


    @Test
    void deleteCourse_WhenEmpty_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/courses/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchCourses_ShouldReturnMatchingCourses() throws Exception {
        Page<Course> page = new PageImpl<>(Arrays.asList(testCourse));
        when(courseService.search(any(String.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/courses/search")
                .param("keyword", "Java")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(testCourse.getTitle()));
    }
}

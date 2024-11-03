package com.formation.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
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
import com.formation.entity.Course;
import com.formation.entity.enums.CourseStatus;
import com.formation.service.CourseService;

@WebMvcTest(CourseController.class)
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        course = Course.builder()
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
    }

    @Test
    void createCourse_WithValidData_ShouldReturnCreated() throws Exception {
        when(courseService.save(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Java Programming")))
                .andExpect(jsonPath("$.level", is("Intermediate")));
    }

    @Test
    void getCourseById_WhenExists_ShouldReturnCourse() throws Exception {
        when(courseService.findById(1L)).thenReturn(course);

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Java Programming")));
    }

    @Test
    void getAllCourses_ShouldReturnPageOfCourses() throws Exception {
        when(courseService.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(course)));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Java Programming")));
    }

    @Test
    void searchCourses_ShouldReturnMatchingCourses() throws Exception {
        when(courseService.search(any(String.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(course)));

        mockMvc.perform(get("/api/courses/search")
                .param("keyword", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Java Programming")));
    }

    @Test
    void findByTrainerId_ShouldReturnTrainersCourses() throws Exception {
        when(courseService.findByTrainerId(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(course)));

        mockMvc.perform(get("/api/courses/trainer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void deleteCourse_WhenEmpty_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());
    }
}

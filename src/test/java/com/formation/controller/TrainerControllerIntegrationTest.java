package com.formation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.entity.Trainer;
import com.formation.service.TrainerService;
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
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TrainerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainerService trainerService;

    private Trainer testTrainer;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testTrainer = Trainer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@test.com")
                .specialty("Java")
                .courses(new HashSet<>())
                .build();
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createTrainer_WithValidData_ShouldReturnCreated() throws Exception {
        when(trainerService.save(any(Trainer.class))).thenReturn(testTrainer);

        mockMvc.perform(post("/api/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTrainer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testTrainer.getId()))
                .andExpect(jsonPath("$.email").value(testTrainer.getEmail()));
    }

    @Test
    void getAllTrainers_ShouldReturnPageOfTrainers() throws Exception {
        Page<Trainer> page = new PageImpl<>(Arrays.asList(testTrainer));
        when(trainerService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/trainers")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testTrainer.getId()));
    }

    @Test
    void getTrainerById_WhenExists_ShouldReturnTrainer() throws Exception {
        when(trainerService.findById(1L)).thenReturn(testTrainer);

        mockMvc.perform(get("/api/trainers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTrainer.getId()));
    }

    @Test
    void findBySpecialty_ShouldReturnTrainersWithSpecialty() throws Exception {
        Page<Trainer> page = new PageImpl<>(Arrays.asList(testTrainer));
        when(trainerService.findBySpecialty(any(String.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/trainers/specialty/{specialty}", "Java")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].specialty").value("Java"));
    }

    @Test
    void findByClassRoomId_ShouldReturnTrainersInClassRoom() throws Exception {
        Page<Trainer> page = new PageImpl<>(Arrays.asList(testTrainer));
        when(trainerService.findByClassRoomId(any(Long.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/trainers/classroom/{classRoomId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testTrainer.getId()));
    }

    @Test
    void deleteTrainer_WhenEmpty_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/trainers/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchTrainers_ShouldReturnMatchingTrainers() throws Exception {
        Page<Trainer> page = new PageImpl<>(Arrays.asList(testTrainer));
        when(trainerService.search(any(String.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/trainers/search")
                .param("keyword", "Java")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].specialty").value(testTrainer.getSpecialty()));
    }

    @Test
    void findAvailableTrainers_ShouldReturnAvailableTrainers() throws Exception {
        Page<Trainer> page = new PageImpl<>(Arrays.asList(testTrainer));
        when(trainerService.findAvailableTrainers(any(Integer.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/trainers/available")
                .param("maxCourses", "5")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testTrainer.getId()));
    }


}

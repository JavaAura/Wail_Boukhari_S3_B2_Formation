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
import com.formation.entity.Trainer;
import com.formation.service.TrainerService;

@WebMvcTest(TrainerController.class)
public class TrainerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = Trainer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@formation.com")
                .specialty("Java Development")
                .build();
    }

    @Test
    void createTrainer_WithValidData_ShouldReturnCreated() throws Exception {
        when(trainerService.save(any(Trainer.class))).thenReturn(trainer);

        mockMvc.perform(post("/api/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.specialty", is("Java Development")));
    }

    @Test
    void getTrainerById_WhenExists_ShouldReturnTrainer() throws Exception {
        when(trainerService.findById(1L)).thenReturn(trainer);

        mockMvc.perform(get("/api/trainers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("john.doe@formation.com")));
    }

    @Test
    void getAllTrainers_ShouldReturnPageOfTrainers() throws Exception {
        when(trainerService.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(trainer)));

        mockMvc.perform(get("/api/trainers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")));
    }

    @Test
    void findByClassRoomId_ShouldReturnTrainersInClassRoom() throws Exception {
        when(trainerService.findByClassRoomId(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(trainer)));

        mockMvc.perform(get("/api/trainers/classroom/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")));
    }

    @Test
    void searchTrainers_ShouldReturnMatchingTrainers() throws Exception {
        when(trainerService.search(any(String.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(trainer)));

        mockMvc.perform(get("/api/trainers/search")
                .param("keyword", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")));
    }

    @Test
    void updateTrainer_WithValidData_ShouldReturnUpdatedTrainer() throws Exception {
        when(trainerService.update(any(Trainer.class))).thenReturn(trainer);

        mockMvc.perform(put("/api/trainers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.specialty", is("Java Development")));
    }

    @Test
    void deleteTrainer_WhenNotAssigned_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/trainers/1"))
                .andExpect(status().isNoContent());
    }

}

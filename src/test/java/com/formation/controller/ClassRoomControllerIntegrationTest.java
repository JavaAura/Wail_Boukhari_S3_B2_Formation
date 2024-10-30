package com.formation.controller;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.entity.ClassRoom;
import com.formation.repository.ClassRoomRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ClassRoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    private ClassRoom classRoom;

    @BeforeEach
    void setUp() {
        classRoom = ClassRoom.builder()
            .name("Integration Test Room")
            .roomNumber("IT101")
            .maxCapacity(30)
            .currentCapacity(0)
            .build();
    }

    @Test
    void createClassRoom_ShouldCreateAndReturnClassRoom() throws Exception {
        mockMvc.perform(post("/api/classrooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(classRoom)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Integration Test Room")))
            .andExpect(jsonPath("$.roomNumber", is("IT101")));
    }

    @Test
    void getClassRoomById_ShouldReturnClassRoom() throws Exception {
        ClassRoom saved = classRoomRepository.save(classRoom);

        mockMvc.perform(get("/api/classrooms/{id}", saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
            .andExpect(jsonPath("$.name", is("Integration Test Room")));
    }


    @Test
    void updateClassRoom_ShouldUpdateAndReturnClassRoom() throws Exception {
        ClassRoom saved = classRoomRepository.save(classRoom);
        saved.setName("Updated Room");

        mockMvc.perform(put("/api/classrooms/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saved)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Updated Room")));
    }

    @Test
    void deleteClassRoom_ShouldDeleteClassRoom() throws Exception {
        ClassRoom saved = classRoomRepository.save(classRoom);

        mockMvc.perform(delete("/api/classrooms/{id}", saved.getId()))
            .andExpect(status().isNoContent());
    }
} 
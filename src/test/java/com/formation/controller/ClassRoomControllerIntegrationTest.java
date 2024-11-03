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
import com.formation.entity.ClassRoom;
import com.formation.service.ClassRoomService;

@WebMvcTest(ClassRoomController.class)
public class ClassRoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClassRoomService classRoomService;

    private ClassRoom classRoom;

    @BeforeEach
    void setUp() {
        classRoom = ClassRoom.builder()
                .id(1L)
                .name("Java Lab")
                .roomNumber("JL-101")
                .currentCapacity(0)
                .maxCapacity(30)
                .students(new HashSet<>())
                .trainers(new HashSet<>())
                .build();
    }

    @Test
    void createClassRoom_WithValidData_ShouldReturnCreated() throws Exception {
        when(classRoomService.save(any(ClassRoom.class))).thenReturn(classRoom);

        mockMvc.perform(post("/api/classrooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(classRoom)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Java Lab")))
                .andExpect(jsonPath("$.roomNumber", is("JL-101")));
    }

    @Test
    void getClassRoomById_WhenExists_ShouldReturnClassRoom() throws Exception {
        when(classRoomService.findById(1L)).thenReturn(classRoom);

        mockMvc.perform(get("/api/classrooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Java Lab")));
    }

    @Test
    void getAllClassRooms_ShouldReturnPageOfClassRooms() throws Exception {
        when(classRoomService.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(classRoom)));

        mockMvc.perform(get("/api/classrooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Java Lab")));
    }

    @Test
    void searchClassRooms_ShouldReturnMatchingClassRooms() throws Exception {
        when(classRoomService.search(any(String.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(classRoom)));

        mockMvc.perform(get("/api/classrooms/search")
                .param("keyword", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Java Lab")));
    }

    @Test
    void findAvailableRooms_ShouldReturnAvailableClassRooms() throws Exception {
        when(classRoomService.findAvailableRooms(any(Integer.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(classRoom)));

        mockMvc.perform(get("/api/classrooms/available")
                .param("capacity", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].maxCapacity", is(30)));
    }

    @Test
    void findEmptyRooms_ShouldReturnEmptyClassRooms() throws Exception {
        when(classRoomService.findEmptyRooms(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(classRoom)));

        mockMvc.perform(get("/api/classrooms/empty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].currentCapacity", is(0)));
    }

    @Test
    void deleteClassRoom_WhenEmpty_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/classrooms/1"))
                .andExpect(status().isNoContent());
    }
}

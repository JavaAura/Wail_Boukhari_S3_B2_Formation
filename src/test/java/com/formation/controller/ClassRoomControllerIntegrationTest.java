package com.formation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.entity.ClassRoom;
import com.formation.service.ClassRoomService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClassRoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClassRoomService classRoomService;

    private ClassRoom testClassRoom;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testClassRoom = ClassRoom.builder()
                .id(1L)
                .name("Java Lab")
                .roomNumber("JL-101")
                .currentCapacity(0)
                .maxCapacity(30)
                .students(new HashSet<>())
                .trainers(new HashSet<>())
                .build();
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createClassRoom_WithValidData_ShouldReturnCreated() throws Exception {
        when(classRoomService.save(any(ClassRoom.class))).thenReturn(testClassRoom);

        mockMvc.perform(post("/api/classrooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testClassRoom)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testClassRoom.getId()))
                .andExpect(jsonPath("$.roomNumber").value(testClassRoom.getRoomNumber()));
    }

    @Test
    void getAllClassRooms_ShouldReturnPageOfClassRooms() throws Exception {
        Page<ClassRoom> page = new PageImpl<>(Arrays.asList(testClassRoom));
        when(classRoomService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/classrooms")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testClassRoom.getId()));
    }

    @Test
    void getClassRoomById_WhenExists_ShouldReturnClassRoom() throws Exception {
        when(classRoomService.findById(1L)).thenReturn(testClassRoom);

        mockMvc.perform(get("/api/classrooms/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testClassRoom.getId()));
    }

@Test
void findAvailableRooms_ShouldReturnAvailableClassRooms() throws Exception {
    Page<ClassRoom> page = new PageImpl<>(Arrays.asList(testClassRoom));
    when(classRoomService.findAvailableRooms(eq(20), any(Pageable.class)))
            .thenReturn(page);

    mockMvc.perform(get("/api/classrooms/available")
            .param("capacity", "20")
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(testClassRoom.getId()));
}

    @Test
    void findEmptyRooms_ShouldReturnEmptyClassRooms() throws Exception {
        Page<ClassRoom> page = new PageImpl<>(Arrays.asList(testClassRoom));
        when(classRoomService.findEmptyRooms(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/classrooms/empty")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testClassRoom.getId()));
    }

    @Test
    void findRoomsWithoutTrainers_ShouldReturnClassRoomsWithoutTrainers() throws Exception {
        Page<ClassRoom> page = new PageImpl<>(Arrays.asList(testClassRoom));
        when(classRoomService.findRoomsWithoutTrainers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/classrooms/without-trainers")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(testClassRoom.getId()));
    }

    @Test
    void deleteClassRoom_WhenEmpty_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/classrooms/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchClassRooms_ShouldReturnMatchingClassRooms() throws Exception {
        Page<ClassRoom> page = new PageImpl<>(Arrays.asList(testClassRoom));
        when(classRoomService.search(any(String.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/classrooms/search")
                .param("keyword", "Java")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(testClassRoom.getName()));
    }
}
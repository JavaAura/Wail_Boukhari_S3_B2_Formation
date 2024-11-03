package com.formation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.formation.entity.ClassRoom;
import com.formation.entity.Student;
import com.formation.entity.Trainer;
import com.formation.repository.ClassRoomRepository;
import com.formation.service.impl.ClassRoomServiceImpl;
import javax.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ClassRoomServiceTest {

    @Mock
    private ClassRoomRepository classRoomRepository;

    @InjectMocks
    private ClassRoomServiceImpl classRoomService;

    private ClassRoom classRoom;
    private Pageable pageable;

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
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void save_ShouldReturnSavedClassRoom() {
        when(classRoomRepository.save(any(ClassRoom.class))).thenReturn(classRoom);
        
        ClassRoom savedClassRoom = classRoomService.save(classRoom);
        
        assertNotNull(savedClassRoom);
        assertEquals(classRoom.getRoomNumber(), savedClassRoom.getRoomNumber());
        verify(classRoomRepository).save(any(ClassRoom.class));
    }

    @Test
    void findById_WhenClassRoomExists_ShouldReturnClassRoom() {
        when(classRoomRepository.findById(1L)).thenReturn(Optional.of(classRoom));
        
        ClassRoom foundClassRoom = classRoomService.findById(1L);
        
        assertNotNull(foundClassRoom);
        assertEquals(classRoom.getId(), foundClassRoom.getId());
    }

    @Test
    void findById_WhenClassRoomDoesNotExist_ShouldThrowException() {
        when(classRoomRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> classRoomService.findById(1L));
    }

    @Test
    void findAll_ShouldReturnPageOfClassRooms() {
        List<ClassRoom> classRooms = Arrays.asList(classRoom);
        Page<ClassRoom> classRoomPage = new PageImpl<>(classRooms);
        
        when(classRoomRepository.findAll(pageable)).thenReturn(classRoomPage);
        
        Page<ClassRoom> result = classRoomService.findAll(pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void delete_WhenClassRoomIsEmpty_ShouldDeleteClassRoom() {
        when(classRoomRepository.findById(1L)).thenReturn(Optional.of(classRoom));
        doNothing().when(classRoomRepository).deleteById(1L);
        
        assertDoesNotThrow(() -> classRoomService.delete(1L));
        verify(classRoomRepository).deleteById(1L);
    }

    @Test
    void delete_WhenClassRoomHasStudents_ShouldThrowException() {
        ClassRoom classRoomWithStudents = classRoom;
        classRoomWithStudents.getStudents().add(new Student());
        
        when(classRoomRepository.findById(1L)).thenReturn(Optional.of(classRoomWithStudents));
        
        assertThrows(EntityNotFoundException.class, () -> classRoomService.delete(1L));
    }

    @Test
    void delete_WhenClassRoomHasTrainers_ShouldThrowException() {
        ClassRoom classRoomWithTrainers = classRoom;
        classRoomWithTrainers.getTrainers().add(new Trainer());
        
        when(classRoomRepository.findById(1L)).thenReturn(Optional.of(classRoomWithTrainers));
        
        assertThrows(EntityNotFoundException.class, () -> classRoomService.delete(1L));
    }

    @Test
    void search_ShouldReturnMatchingClassRooms() {
        List<ClassRoom> classRooms = Arrays.asList(classRoom);
        Page<ClassRoom> classRoomPage = new PageImpl<>(classRooms);
        
        when(classRoomRepository.search("Java", pageable)).thenReturn(classRoomPage);
        
        Page<ClassRoom> result = classRoomService.search("Java", pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findAvailableRooms_ShouldReturnAvailableRooms() {
        List<ClassRoom> classRooms = Arrays.asList(classRoom);
        Page<ClassRoom> classRoomPage = new PageImpl<>(classRooms);
        
        when(classRoomRepository.findAvailableRooms(20, pageable)).thenReturn(classRoomPage);
        
        Page<ClassRoom> result = classRoomService.findAvailableRooms(20, pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findEmptyRooms_ShouldReturnEmptyRooms() {
        List<ClassRoom> classRooms = Arrays.asList(classRoom);
        Page<ClassRoom> classRoomPage = new PageImpl<>(classRooms);
        
        when(classRoomRepository.findEmptyRooms(pageable)).thenReturn(classRoomPage);
        
        Page<ClassRoom> result = classRoomService.findEmptyRooms(pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findRoomsWithoutTrainers_ShouldReturnRoomsWithoutTrainers() {
        List<ClassRoom> classRooms = Arrays.asList(classRoom);
        Page<ClassRoom> classRoomPage = new PageImpl<>(classRooms);
        
        when(classRoomRepository.findRoomsWithoutTrainers(pageable)).thenReturn(classRoomPage);
        
        Page<ClassRoom> result = classRoomService.findRoomsWithoutTrainers(pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}

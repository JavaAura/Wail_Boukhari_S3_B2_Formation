package com.formation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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

import com.formation.entity.Student;
import com.formation.repository.StudentRepository;
import com.formation.service.impl.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .level("Intermediate")
                .build();
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void save_ShouldReturnSavedStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        
        Student savedStudent = studentService.save(student);
        
        assertNotNull(savedStudent);
        assertEquals(student.getEmail(), savedStudent.getEmail());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void findById_WhenStudentExists_ShouldReturnStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        
        Student foundStudent = studentService.findById(1L);
        
        assertNotNull(foundStudent);
        assertEquals(student.getId(), foundStudent.getId());
    }

    @Test
    void findByLevel_ShouldReturnStudentsWithLevel() {
        List<Student> students = Arrays.asList(student);
        Page<Student> studentPage = new PageImpl<>(students);
        
        when(studentRepository.findByLevel("Intermediate", pageable)).thenReturn(studentPage);
        
        Page<Student> result = studentService.findByLevel("Intermediate", pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Intermediate", result.getContent().get(0).getLevel());
    }

    @Test
    void findByClassRoomId_ShouldReturnStudentsInClassRoom() {
        List<Student> students = Arrays.asList(student);
        Page<Student> studentPage = new PageImpl<>(students);
        
        when(studentRepository.findByClassRoomId(1L, pageable)).thenReturn(studentPage);
        
        Page<Student> result = studentService.findByClassRoomId(1L, pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void search_ShouldReturnMatchingStudents() {
        List<Student> students = Arrays.asList(student);
        Page<Student> studentPage = new PageImpl<>(students);
        
        when(studentRepository.search("John", pageable)).thenReturn(studentPage);
        
        Page<Student> result = studentService.search("John", pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}

package com.formation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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

import com.formation.entity.Course;
import com.formation.entity.enums.CourseStatus;
import com.formation.repository.CourseRepository;
import com.formation.service.impl.CourseServiceImpl;
import javax.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private Pageable pageable;

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
                .build();
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void save_ShouldReturnSavedCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        
        Course savedCourse = courseService.save(course);
        
        assertNotNull(savedCourse);
        assertEquals(course.getTitle(), savedCourse.getTitle());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void findById_WhenCourseExists_ShouldReturnCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        
        Course foundCourse = courseService.findById(1L);
        
        assertNotNull(foundCourse);
        assertEquals(course.getId(), foundCourse.getId());
    }

    @Test
    void findById_WhenCourseDoesNotExist_ShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> courseService.findById(1L));
    }

    @Test
    void findByDateRange_ShouldReturnCoursesInRange() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(1);
        List<Course> courses = Arrays.asList(course);
        Page<Course> coursePage = new PageImpl<>(courses);
        
        when(courseRepository.findByDateRange(startDate, endDate, pageable)).thenReturn(coursePage);
        
        Page<Course> result = courseService.findByDateRange(startDate, endDate, pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findAvailableCourses_ShouldReturnAvailableCourses() {
        List<Course> courses = Arrays.asList(course);
        Page<Course> coursePage = new PageImpl<>(courses);
        
        when(courseRepository.findAvailableCourses(pageable)).thenReturn(coursePage);
        
        Page<Course> result = courseService.findAvailableCourses(pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findUpcomingCourses_ShouldReturnUpcomingCourses() {
        List<Course> courses = Arrays.asList(course);
        Page<Course> coursePage = new PageImpl<>(courses);
        
        when(courseRepository.findUpcomingCourses(pageable)).thenReturn(coursePage);
        
        Page<Course> result = courseService.findUpcomingCourses(pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}

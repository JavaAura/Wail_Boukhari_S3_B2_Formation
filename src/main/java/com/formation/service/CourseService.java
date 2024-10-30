package com.formation.service;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.formation.entity.Course;

public interface CourseService {
    Course save(Course course);
    Course findById(Long id);
    Page<Course> findAll(Pageable pageable);
    Course update(Course course);
    void delete(Long id);
    Page<Course> search(String keyword, Pageable pageable);
    Page<Course> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Course> findAvailableCourses(Pageable pageable);
    Page<Course> findByCapacityRange(int capacity, Pageable pageable);
    Page<Course> findUpcomingCourses(Pageable pageable);
    Page<Course> findOngoingCourses(Pageable pageable);
    Page<Course> findByTrainerId(Long trainerId, Pageable pageable);
    Page<Course> findCoursesWithoutTrainer(Pageable pageable);
}

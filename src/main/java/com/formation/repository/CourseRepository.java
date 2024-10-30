package com.formation.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.formation.entity.Course;
import com.formation.entity.enums.CourseStatus;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Basic finder methods
    Page<Course> findByTitle(String title, Pageable pageable);
    Page<Course> findByLevel(String level, Pageable pageable);
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
    
    // Search and date range queries
    @Query("SELECT c FROM Course c WHERE c.title LIKE %:keyword% OR c.level LIKE %:keyword% OR c.prerequisites LIKE %:keyword%")
    Page<Course> search(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.startDate >= :startDate AND c.endDate <= :endDate")
    Page<Course> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    // Capacity and availability queries
    @Query("SELECT c FROM Course c WHERE SIZE(c.students) < c.maxCapacity AND c.status = 'PLANNED'")
    Page<Course> findAvailableCourses(Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.minCapacity <= :capacity AND c.maxCapacity >= :capacity")
    Page<Course> findByCapacityRange(@Param("capacity") int capacity, Pageable pageable);
    
    // Status-based queries
    @Query("SELECT c FROM Course c WHERE c.status = 'PLANNED' AND c.startDate > CURRENT_DATE")
    Page<Course> findUpcomingCourses(Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.status = 'IN_PROGRESS'")
    Page<Course> findOngoingCourses(Pageable pageable);
    
    // Trainer-related queries
    @Query("SELECT c FROM Course c WHERE c.trainer.id = :trainerId")
    Page<Course> findByTrainerId(@Param("trainerId") Long trainerId, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.trainer IS NULL")
    Page<Course> findCoursesWithoutTrainer(Pageable pageable);
}

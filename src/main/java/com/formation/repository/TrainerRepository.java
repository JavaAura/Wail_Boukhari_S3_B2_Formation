package com.formation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.formation.entity.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    // Basic finder methods
    Page<Trainer> findByEmail(String email, Pageable pageable);
    Page<Trainer> findBySpecialty(String specialty, Pageable pageable);
    Page<Trainer> findByLastNameAndFirstName(String lastName, String firstName, Pageable pageable);
    
    // Search query
    @Query("SELECT t FROM Trainer t WHERE t.lastName LIKE %:keyword% OR t.firstName LIKE %:keyword% OR t.specialty LIKE %:keyword%")
    Page<Trainer> search(@Param("keyword") String keyword, Pageable pageable);
    
    // ClassRoom-related queries
    @Query("SELECT t FROM Trainer t WHERE t.classRoom.id = :classRoomId")
    Page<Trainer> findByClassRoomId(@Param("classRoomId") Long classRoomId, Pageable pageable);
    
    // Course-related queries
    @Query("SELECT t FROM Trainer t WHERE (SELECT COUNT(c) FROM Course c WHERE c.trainer = t) < :maxCourses")
    Page<Trainer> findAvailableTrainers(@Param("maxCourses") int maxCourses, Pageable pageable);
    
    @Query("SELECT t FROM Trainer t WHERE (SELECT COUNT(c) FROM Course c WHERE c.trainer = t) = 0")
    Page<Trainer> findTrainersWithoutCourses(Pageable pageable);
    
    // Validation
    boolean existsByEmail(String email);
}

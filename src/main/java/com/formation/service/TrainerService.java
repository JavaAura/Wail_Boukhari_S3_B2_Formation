package com.formation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.formation.entity.Trainer;

public interface TrainerService {
    // Basic CRUD operations
    Trainer save(Trainer trainer);
    Trainer findById(Long id);
    Page<Trainer> findAll(Pageable pageable);
    Trainer update(Trainer trainer);
    void delete(Long id);
    
    // Search operations
    Page<Trainer> search(String keyword, Pageable pageable);
    Page<Trainer> findByEmail(String email, Pageable pageable);
    Page<Trainer> findBySpecialty(String specialty, Pageable pageable);
    Page<Trainer> findByLastNameAndFirstName(String lastName, String firstName, Pageable pageable);
    
    // ClassRoom-related operations
    Page<Trainer> findByClassRoomId(Long classRoomId, Pageable pageable);
    
    // Course-related operations
    Page<Trainer> findAvailableTrainers(int maxCourses, Pageable pageable);
    Page<Trainer> findTrainersWithoutCourses(Pageable pageable);
}

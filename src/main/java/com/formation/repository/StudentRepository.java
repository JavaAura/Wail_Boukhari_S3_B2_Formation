package com.formation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.formation.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Basic finders
    Page<Student> findByEmail(String email, Pageable pageable);
    Page<Student> findByLevel(String level, Pageable pageable);
    
    // Multiple criteria
    Page<Student> findByLastNameAndFirstName(String lastName, String firstName, Pageable pageable);
    Page<Student> findByLevelAndEmail(String level, String email, Pageable pageable);
    
    // Custom queries
    @Query("SELECT s FROM Student s WHERE s.lastName LIKE %:keyword% OR s.firstName LIKE %:keyword% OR s.email LIKE %:keyword%")
    Page<Student> search(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.course.id = :courseId")
    Page<Student> findByCourseId(@Param("courseId") Long courseId, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.classRoom.id = :classRoomId")
    Page<Student> findByClassRoomId(@Param("classRoomId") Long classRoomId, Pageable pageable);
    
    // Validation
    boolean existsByEmail(String email);
}

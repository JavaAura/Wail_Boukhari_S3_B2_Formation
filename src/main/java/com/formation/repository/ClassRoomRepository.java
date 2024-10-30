package com.formation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.formation.entity.ClassRoom;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    // Basic finder methods
    Page<ClassRoom> findByName(String name, Pageable pageable);
    Page<ClassRoom> findByRoomNumber(String roomNumber, Pageable pageable);
    
    // Search query
    @Query("SELECT c FROM ClassRoom c WHERE c.name LIKE %:keyword% OR c.roomNumber LIKE %:keyword%")
    Page<ClassRoom> search(@Param("keyword") String keyword, Pageable pageable);
    
    // Capacity queries
    @Query("SELECT c FROM ClassRoom c WHERE (SELECT COUNT(s) FROM Student s WHERE s.classRoom = c) < :capacity")
    Page<ClassRoom> findAvailableRooms(@Param("capacity") int capacity, Pageable pageable);
    
    @Query("SELECT c FROM ClassRoom c WHERE (SELECT COUNT(s) FROM Student s WHERE s.classRoom = c) = 0")
    Page<ClassRoom> findEmptyRooms(Pageable pageable);
    
    // Trainer-related queries
    @Query("SELECT c FROM ClassRoom c WHERE (SELECT COUNT(t) FROM Trainer t WHERE t.classRoom = c) = 0")
    Page<ClassRoom> findRoomsWithoutTrainers(Pageable pageable);
    
    // Validation
    boolean existsByRoomNumber(String roomNumber);
}

package com.formation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.formation.entity.ClassRoom;

public interface ClassRoomService {
    ClassRoom save(ClassRoom classRoom);
    ClassRoom findById(Long id);
    Page<ClassRoom> findAll(Pageable pageable);
    ClassRoom update(ClassRoom classRoom);
    void delete(Long id);
    Page<ClassRoom> search(String keyword, Pageable pageable);
    Page<ClassRoom> findAvailableRooms(int capacity, Pageable pageable);
    Page<ClassRoom> findEmptyRooms(Pageable pageable);
    Page<ClassRoom> findRoomsWithoutTrainers(Pageable pageable);
}

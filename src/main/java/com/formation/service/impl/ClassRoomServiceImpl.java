package com.formation.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formation.entity.ClassRoom;
import com.formation.repository.ClassRoomRepository;
import com.formation.service.ClassRoomService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class ClassRoomServiceImpl implements ClassRoomService {

    private final ClassRoomRepository classRoomRepository;

    public ClassRoomServiceImpl(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    @Override
    public ClassRoom save(ClassRoom classRoom) {
        validateClassRoom(classRoom);
        if (classRoomRepository.existsByRoomNumber(classRoom.getRoomNumber())) {
            throw new EntityExistsException("Classroom with room number " + classRoom.getRoomNumber() + " already exists");
        }
        return classRoomRepository.save(classRoom);
    }

    @Override
    public ClassRoom findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return classRoomRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Classroom not found with id: " + id));
    }

    @Override
    public Page<ClassRoom> findAll(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        return classRoomRepository.findAll(pageable);
    }

    @Override
    public ClassRoom update(ClassRoom classRoom) {
        validateClassRoom(classRoom);
        ClassRoom existingClassRoom = findById(classRoom.getId());
        
        if (!existingClassRoom.getRoomNumber().equals(classRoom.getRoomNumber()) && 
            classRoomRepository.existsByRoomNumber(classRoom.getRoomNumber())) {
            throw new EntityExistsException("Classroom with room number " + classRoom.getRoomNumber() + " already exists");
        }
        
        return classRoomRepository.save(classRoom);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        ClassRoom classRoom = findById(id);
        validateDeletion(classRoom);
        classRoomRepository.deleteById(id);
    }

    @Override
    public Page<ClassRoom> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        return classRoomRepository.search(keyword, pageable);
    }

    @Override
    public Page<ClassRoom> findAvailableRooms(int capacity, Pageable pageable) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        return classRoomRepository.findAvailableRooms(capacity, pageable);
    }

    @Override
    public Page<ClassRoom> findEmptyRooms(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        return classRoomRepository.findEmptyRooms(pageable);
    }

    @Override
    public Page<ClassRoom> findRoomsWithoutTrainers(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        return classRoomRepository.findRoomsWithoutTrainers(pageable);
    }

    private void validateClassRoom(ClassRoom classRoom) {
        if (classRoom == null) {
            throw new IllegalArgumentException("ClassRoom cannot be null");
        }
        if (classRoom.getRoomNumber() == null || classRoom.getRoomNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be empty");
        }
        if (classRoom.getName() == null || classRoom.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Classroom name cannot be empty");
        }
        if (classRoom.getMaxCapacity() <= 0) {
            throw new IllegalArgumentException("Max capacity must be greater than 0");
        }
    }

    private void validateDeletion(ClassRoom classRoom) {
        if (!classRoom.getStudents().isEmpty()) {
            throw new EntityNotFoundException("Cannot delete classroom with enrolled students");
        }
        if (!classRoom.getTrainers().isEmpty()) {
            throw new EntityNotFoundException("Cannot delete classroom with assigned trainers");
        }
    }
}
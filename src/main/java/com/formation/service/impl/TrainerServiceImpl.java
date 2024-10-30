package com.formation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formation.entity.Trainer;
import com.formation.exception.BusinessException;
import com.formation.exception.ResourceNotFoundException;
import com.formation.repository.TrainerRepository;
import com.formation.service.TrainerService;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {
    
    @Autowired
    private TrainerRepository trainerRepository;
    
    @Override
    public Trainer save(Trainer trainer) {
        if (trainerRepository.existsByEmail(trainer.getEmail())) {
            throw new BusinessException("A trainer with email " + trainer.getEmail() + " already exists");
        }
        return trainerRepository.save(trainer);
    }
    
    @Override
    public Trainer findById(Long id) {
        return trainerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + id));
    }
    
    @Override
    public Page<Trainer> findAll(Pageable pageable) {
        return trainerRepository.findAll(pageable);
    }
    
    @Override
    public Trainer update(Trainer trainer) {
        Trainer existingTrainer = findById(trainer.getId());
        
        if (!existingTrainer.getEmail().equals(trainer.getEmail()) && 
            trainerRepository.existsByEmail(trainer.getEmail())) {
            throw new BusinessException("A trainer with email " + trainer.getEmail() + " already exists");
        }
        
        return trainerRepository.save(trainer);
    }
    
    @Override
    public void delete(Long id) {
        Trainer trainer = findById(id);
        if (!trainer.getCourses().isEmpty()) {
            throw new BusinessException("Cannot delete trainer with assigned courses");
        }
        trainerRepository.deleteById(id);
    }
    
    @Override
    public Page<Trainer> search(String keyword, Pageable pageable) {
        return trainerRepository.search(keyword, pageable);
    }
    
    @Override
    public Page<Trainer> findByEmail(String email, Pageable pageable) {
        return trainerRepository.findByEmail(email, pageable);
    }
    
    @Override
    public Page<Trainer> findBySpecialty(String specialty, Pageable pageable) {
        return trainerRepository.findBySpecialty(specialty, pageable);
    }
    
    @Override
    public Page<Trainer> findByLastNameAndFirstName(String lastName, String firstName, Pageable pageable) {
        return trainerRepository.findByLastNameAndFirstName(lastName, firstName, pageable);
    }
    
    @Override
    public Page<Trainer> findByClassRoomId(Long classRoomId, Pageable pageable) {
        return trainerRepository.findByClassRoomId(classRoomId, pageable);
    }
    
    @Override
    public Page<Trainer> findAvailableTrainers(int maxCourses, Pageable pageable) {
        return trainerRepository.findAvailableTrainers(maxCourses, pageable);
    }
    
    @Override
    public Page<Trainer> findTrainersWithoutCourses(Pageable pageable) {
        return trainerRepository.findTrainersWithoutCourses(pageable);
    }
}

package com.formation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

import com.formation.entity.Trainer;
import com.formation.repository.TrainerRepository;
import com.formation.service.impl.TrainerServiceImpl;
import javax.persistence.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        trainer = Trainer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@formation.com")
                .specialty("Java Development")
                .build();
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void save_ShouldReturnSavedTrainer() {
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        
        Trainer savedTrainer = trainerService.save(trainer);
        
        assertNotNull(savedTrainer);
        assertEquals(trainer.getEmail(), savedTrainer.getEmail());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void findById_WhenTrainerExists_ShouldReturnTrainer() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        
        Trainer foundTrainer = trainerService.findById(1L);
        
        assertNotNull(foundTrainer);
        assertEquals(trainer.getId(), foundTrainer.getId());
    }

    @Test
    void findById_WhenTrainerDoesNotExist_ShouldThrowException() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> trainerService.findById(1L));
    }

    @Test
    void findAll_ShouldReturnPageOfTrainers() {
        List<Trainer> trainers = Arrays.asList(trainer);
        Page<Trainer> trainerPage = new PageImpl<>(trainers);
        
        when(trainerRepository.findAll(pageable)).thenReturn(trainerPage);
        
        Page<Trainer> result = trainerService.findAll(pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void update_WhenTrainerExists_ShouldReturnUpdatedTrainer() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        
        trainer.setSpecialty("Spring Boot Development");
        Trainer updatedTrainer = trainerService.update(trainer);
        
        assertNotNull(updatedTrainer);
        assertEquals("Spring Boot Development", updatedTrainer.getSpecialty());
    }

    @Test
    void delete_WhenTrainerExists_ShouldDeleteTrainer() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        doNothing().when(trainerRepository).deleteById(1L);
        
        assertDoesNotThrow(() -> trainerService.delete(1L));
        verify(trainerRepository).deleteById(1L);
    }

    @Test
    void search_ShouldReturnMatchingTrainers() {
        String keyword = "Java";
        List<Trainer> trainers = Arrays.asList(trainer);
        Page<Trainer> trainerPage = new PageImpl<>(trainers);
        
        when(trainerRepository.search(keyword, pageable)).thenReturn(trainerPage);
        
        Page<Trainer> result = trainerService.search(keyword, pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findBySpecialty_ShouldReturnTrainersWithSpecialty() {
        String specialty = "Java Development";
        List<Trainer> trainers = Arrays.asList(trainer);
        Page<Trainer> trainerPage = new PageImpl<>(trainers);
        
        when(trainerRepository.findBySpecialty(specialty, pageable)).thenReturn(trainerPage);
        
        Page<Trainer> result = trainerService.findBySpecialty(specialty, pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(specialty, result.getContent().get(0).getSpecialty());
    }
}

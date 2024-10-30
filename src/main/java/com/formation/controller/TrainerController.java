package com.formation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.entity.Trainer;
import com.formation.service.TrainerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/trainers")
@Tag(name = "Trainer Management", description = "APIs for managing trainers")
public class TrainerController {
    @Autowired
    private TrainerService trainerService;

    @Operation(summary = "Create a new trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Trainer created successfully",
            content = @Content(schema = @Schema(implementation = Trainer.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Trainer> createTrainer(
            @Parameter(description = "Trainer to create") @Valid @RequestBody Trainer trainer) {
        return new ResponseEntity<>(trainerService.save(trainer), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a trainer by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer found"),
        @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Trainer> getTrainerById(
            @Parameter(description = "ID of the trainer") @PathVariable Long id) {
        return ResponseEntity.ok(trainerService.findById(id));
    }

    @Operation(summary = "Get all trainers with pagination")
    @ApiResponse(responseCode = "200", description = "List of trainers retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<Trainer>> getAllTrainers(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(trainerService.findAll(pageable));
    }

    @Operation(summary = "Update a trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer updated successfully"),
        @ApiResponse(responseCode = "404", description = "Trainer not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Trainer> updateTrainer(
            @Parameter(description = "ID of the trainer to update") @PathVariable Long id,
            @Parameter(description = "Updated trainer details") @Valid @RequestBody Trainer trainer) {
        trainer.setId(id);
        return ResponseEntity.ok(trainerService.update(trainer));
    }

    @Operation(summary = "Delete a trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Trainer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainer(
            @Parameter(description = "ID of the trainer to delete") @PathVariable Long id) {
        trainerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search trainers by keyword")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<Page<Trainer>> searchTrainers(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(trainerService.search(keyword, pageable));
    }

    @Operation(summary = "Get trainers by email")
    @ApiResponse(responseCode = "200", description = "Trainers retrieved successfully")
    @GetMapping("/email/{email}")
    public ResponseEntity<Page<Trainer>> getTrainersByEmail(
            @Parameter(description = "Trainer's email") @PathVariable String email,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(trainerService.findByEmail(email, pageable));
    }

    @Operation(summary = "Get trainers by specialty")
    @ApiResponse(responseCode = "200", description = "Trainers retrieved successfully")
    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<Page<Trainer>> getTrainersBySpecialty(
            @Parameter(description = "Trainer's specialty") @PathVariable String specialty,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(trainerService.findBySpecialty(specialty, pageable));
    }

    @Operation(summary = "Get trainers by name")
    @ApiResponse(responseCode = "200", description = "Trainers retrieved successfully")
    @GetMapping("/name")
    public ResponseEntity<Page<Trainer>> getTrainersByName(
            @Parameter(description = "Trainer's last name") @RequestParam String lastName,
            @Parameter(description = "Trainer's first name") @RequestParam String firstName,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(trainerService.findByLastNameAndFirstName(lastName, firstName, pageable));
    }

    @Operation(summary = "Get trainers by classroom")
    @ApiResponse(responseCode = "200", description = "Trainers retrieved successfully")
    @GetMapping("/classroom/{classRoomId}")
    public ResponseEntity<Page<Trainer>> getTrainersByClassRoom(
            @Parameter(description = "Classroom ID") @PathVariable Long classRoomId,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(trainerService.findByClassRoomId(classRoomId, pageable));
    }

    @Operation(summary = "Get available trainers")
    @ApiResponse(responseCode = "200", description = "Available trainers retrieved successfully")
    @GetMapping("/available")
    public ResponseEntity<Page<Trainer>> getAvailableTrainers(
            @Parameter(description = "Maximum number of courses") @RequestParam int maxCourses,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(trainerService.findAvailableTrainers(maxCourses, pageable));
    }

    @Operation(summary = "Get trainers without courses")
    @ApiResponse(responseCode = "200", description = "Trainers without courses retrieved successfully")
    @GetMapping("/without-courses")
    public ResponseEntity<Page<Trainer>> getTrainersWithoutCourses(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(trainerService.findTrainersWithoutCourses(pageable));
    }
}

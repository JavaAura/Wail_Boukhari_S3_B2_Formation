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

import com.formation.entity.ClassRoom;
import com.formation.service.ClassRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/classrooms")
@Tag(name = "Classroom Management", description = "APIs for managing classrooms")
public class ClassRoomController {

    @Autowired
    private ClassRoomService classRoomService;

    @Operation(summary = "Create a new classroom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Classroom created successfully",
            content = @Content(schema = @Schema(implementation = ClassRoom.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<ClassRoom> createClassRoom(
            @Parameter(description = "Classroom to create") @Valid @RequestBody ClassRoom classRoom) {
        return new ResponseEntity<>(classRoomService.save(classRoom), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a classroom by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classroom found"),
        @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClassRoom> getClassRoomById(
            @Parameter(description = "ID of the classroom") @PathVariable Long id) {
        return ResponseEntity.ok(classRoomService.findById(id));
    }

    @Operation(summary = "Get all classrooms with pagination")
    @ApiResponse(responseCode = "200", description = "List of classrooms retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<ClassRoom>> getAllClassRooms(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(classRoomService.findAll(pageable));
    }

    @Operation(summary = "Update a classroom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classroom updated successfully"),
        @ApiResponse(responseCode = "404", description = "Classroom not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClassRoom> updateClassRoom(
            @Parameter(description = "ID of the classroom to update") @PathVariable Long id,
            @Parameter(description = "Updated classroom details") @Valid @RequestBody ClassRoom classRoom) {
        classRoom.setId(id);
        return ResponseEntity.ok(classRoomService.update(classRoom));
    }

    @Operation(summary = "Delete a classroom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Classroom deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassRoom(
            @Parameter(description = "ID of the classroom to delete") @PathVariable Long id) {
        classRoomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search classrooms by keyword")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<Page<ClassRoom>> searchClassRooms(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(classRoomService.search(keyword, pageable));
    }

    @Operation(summary = "Get available rooms by capacity")
    @ApiResponse(responseCode = "200", description = "Available rooms retrieved successfully")
    @GetMapping("/available")
    public ResponseEntity<Page<ClassRoom>> getAvailableRooms(
            @Parameter(description = "Required capacity") @RequestParam int capacity,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(classRoomService.findAvailableRooms(capacity, pageable));
    }

    @Operation(summary = "Get empty classrooms")
    @ApiResponse(responseCode = "200", description = "Empty rooms retrieved successfully")
    @GetMapping("/empty")
    public ResponseEntity<Page<ClassRoom>> getEmptyRooms(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(classRoomService.findEmptyRooms(pageable));
    }

    @Operation(summary = "Get classrooms without trainers")
    @ApiResponse(responseCode = "200", description = "Classrooms without trainers retrieved successfully")
    @GetMapping("/without-trainers")
    public ResponseEntity<Page<ClassRoom>> getRoomsWithoutTrainers(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(classRoomService.findRoomsWithoutTrainers(pageable));
    }
}

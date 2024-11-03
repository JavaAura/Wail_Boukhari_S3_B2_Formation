package com.formation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.entity.ClassRoom;
import com.formation.exception.DuplicateResourceException;
import com.formation.exception.ExceptionCode;
import com.formation.exception.ResourceInUseException;
import com.formation.exception.ResourceNotFoundException;
import com.formation.exception.ValidationException;
import com.formation.service.ClassRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/classrooms")
@Tag(name = "Classroom Management", description = "APIs for managing classrooms")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, 
    RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ClassRoomController {

    @Autowired
    private ClassRoomService classRoomService;

    @Operation(summary = "Create a new classroom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Classroom created successfully",
            content = @Content(schema = @Schema(implementation = ClassRoom.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation failed"),
        @ApiResponse(responseCode = "409", description = "Classroom with same name/number already exists")
    })
    @PostMapping
    public ResponseEntity<ClassRoom> createClassRoom(
            @Parameter(description = "Classroom to create", required = true) 
            @Valid @RequestBody(required = true) ClassRoom classRoom) {
        if (classRoom == null) {
            throw new ValidationException(ExceptionCode.NULL_REQUEST);
        }
        try {
            return new ResponseEntity<>(classRoomService.save(classRoom), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new DuplicateResourceException(ExceptionCode.CLASSROOM_NUMBER_EXISTS, 
                classRoom.getRoomNumber());
        }
    }

    @Operation(summary = "Get a classroom by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classroom found"),
        @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClassRoom> getClassRoomById(
            @Parameter(description = "ID of the classroom") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(classRoomService.findById(id));
        } catch (Exception e) {
            throw new ResourceNotFoundException(ExceptionCode.CLASSROOM_NOT_FOUND, id);
        }
    }

    @Operation(summary = "Get all classrooms with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of classrooms retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No classrooms found"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping
    public ResponseEntity<Page<ClassRoom>> getAllClassRooms(
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<ClassRoom> classrooms = classRoomService.findAll(pageable);
        return classrooms.hasContent() 
            ? ResponseEntity.ok(classrooms)
            : ResponseEntity.noContent().build();
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
        if (classRoom == null) {
            throw new ValidationException(ExceptionCode.NULL_REQUEST);
        }
        try {
            classRoom.setId(id);
            return ResponseEntity.ok(classRoomService.update(classRoom));
        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                throw new ResourceNotFoundException(ExceptionCode.CLASSROOM_NOT_FOUND, id);
            }
            throw new DuplicateResourceException(ExceptionCode.CLASSROOM_NUMBER_EXISTS, 
                classRoom.getRoomNumber());
        }
    }

    @Operation(summary = "Delete a classroom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Classroom deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Classroom not found"),
        @ApiResponse(responseCode = "409", description = "Classroom cannot be deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassRoom(
            @Parameter(description = "ID of the classroom to delete") @PathVariable Long id) {
        try {
            classRoomService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            if (e.getMessage().contains("enrolled students")) {
                throw new ResourceInUseException(ExceptionCode.CLASSROOM_WITH_STUDENTS);
            } else if (e.getMessage().contains("assigned trainers")) {
                throw new ResourceInUseException(ExceptionCode.CLASSROOM_WITH_TRAINERS);
            }
            throw new ResourceNotFoundException(ExceptionCode.CLASSROOM_NOT_FOUND, id);
        }
    }

    @Operation(summary = "Search classrooms by keyword")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ClassRoom>> searchClassRooms(
            @Parameter(description = "Search keyword") 
            @RequestParam(required = true) @NotBlank String keyword,
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        if (keyword.trim().length() < 2) {
            throw new ValidationException(ExceptionCode.INVALID_SEARCH, 2);
        }
        Page<ClassRoom> results = classRoomService.search(keyword, pageable);
        return results.hasContent() ? ResponseEntity.ok(results) : ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get available rooms by capacity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available rooms retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid capacity parameter")
    })
    @GetMapping("/available")
    public ResponseEntity<Page<ClassRoom>> getAvailableRooms(
            @Parameter(description = "Required capacity") 
            @RequestParam @Min(value = 1, message = "Capacity must be at least 1") int capacity,
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "currentCapacity") Pageable pageable) {
        if (capacity < 1) {
            throw new ValidationException(ExceptionCode.INVALID_CAPACITY, 1, Integer.MAX_VALUE);
        }
        Page<ClassRoom> rooms = classRoomService.findAvailableRooms(capacity, pageable);
        return rooms.hasContent() ? ResponseEntity.ok(rooms) : ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get empty classrooms")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empty rooms retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No empty rooms found"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/empty")
    public ResponseEntity<Page<ClassRoom>> getEmptyRooms(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        try {
            Page<ClassRoom> rooms = classRoomService.findEmptyRooms(pageable);
            return rooms.hasContent() ? ResponseEntity.ok(rooms) : ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ValidationException(ExceptionCode.INVALID_ROOM_NUMBER, "Invalid pagination parameters");
        }
    }

    @Operation(summary = "Get classrooms without trainers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classrooms without trainers retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No classrooms without trainers found"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/without-trainers")
    public ResponseEntity<Page<ClassRoom>> getRoomsWithoutTrainers(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        try {
            Page<ClassRoom> rooms = classRoomService.findRoomsWithoutTrainers(pageable);
            return rooms.hasContent() ? ResponseEntity.ok(rooms) : ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ValidationException(ExceptionCode.INVALID_ROOM_NUMBER, "Invalid pagination parameters");
        }
    }
}

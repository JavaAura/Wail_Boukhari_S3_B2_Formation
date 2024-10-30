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

import com.formation.entity.Student;
import com.formation.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Management", description = "APIs for managing students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Student created successfully",
            content = @Content(schema = @Schema(implementation = Student.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Student> createStudent(
            @Parameter(description = "Student to create") @Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentService.save(student), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a student by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @Parameter(description = "ID of the student") @PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @Operation(summary = "Get all students with pagination")
    @ApiResponse(responseCode = "200", description = "List of students retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<Student>> getAllStudents(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(studentService.findAll(pageable));
    }

    @Operation(summary = "Update a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student updated successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @Parameter(description = "ID of the student to update") @PathVariable Long id,
            @Parameter(description = "Updated student details") @Valid @RequestBody Student student) {
        student.setId(id);
        return ResponseEntity.ok(studentService.update(student));
    }

    @Operation(summary = "Delete a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "ID of the student to delete") @PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search students by keyword")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<Page<Student>> searchStudents(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(studentService.search(keyword, pageable));
    }

    @Operation(summary = "Get students by level")
    @ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    @GetMapping("/level/{level}")
    public ResponseEntity<Page<Student>> getStudentsByLevel(
            @Parameter(description = "Student level")
            @PathVariable String level,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(studentService.findByLevel(level, pageable));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<Student>> getStudentsByCourse(
            @PathVariable Long courseId, 
            Pageable pageable) {
        return ResponseEntity.ok(studentService.findByCourseId(courseId, pageable));
    }

    @GetMapping("/classroom/{classRoomId}")
    public ResponseEntity<Page<Student>> getStudentsByClassRoom(
            @PathVariable Long classRoomId, 
            Pageable pageable) {
        return ResponseEntity.ok(studentService.findByClassRoomId(classRoomId, pageable));
    }

    @GetMapping("/name")
    public ResponseEntity<Page<Student>> getStudentsByName(
            @RequestParam String lastName,
            @RequestParam String firstName,
            Pageable pageable) {
        return ResponseEntity.ok(studentService.findByLastNameAndFirstName(lastName, firstName, pageable));
    }
}

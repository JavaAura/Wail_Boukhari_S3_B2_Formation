package com.formation.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.entity.Course;
import com.formation.service.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")  
@Tag(name = "Course Management", description = "APIs for managing training courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Create a new training course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course created successfully",
            content = @Content(schema = @Schema(implementation = Course.class))),
        @ApiResponse(responseCode = "400", description = "Invalid course data")
    })
    @PostMapping
    public ResponseEntity<Course> createCourse(
            @Parameter(description = "Course details including title, level, prerequisites, capacity") 
            @Valid @RequestBody Course course) {
        return new ResponseEntity<>(courseService.save(course), HttpStatus.CREATED);
    }

    @Operation(summary = "Get course details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course details retrieved"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(
            @Parameter(description = "Course ID") @PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @Operation(summary = "Get all courses with pagination")
    @ApiResponse(responseCode = "200", description = "List of courses retrieved")
    @GetMapping
    public ResponseEntity<Page<Course>> getAllCourses(Pageable pageable) {
        return ResponseEntity.ok(courseService.findAll(pageable));
    }

    @Operation(summary = "Update course details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course updated"),
        @ApiResponse(responseCode = "404", description = "Course not found"),
        @ApiResponse(responseCode = "400", description = "Invalid course data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @Parameter(description = "Course ID") @PathVariable Long id,
            @Parameter(description = "Updated course details") @Valid @RequestBody Course course) {
        course.setId(id);
        return ResponseEntity.ok(courseService.update(course));
    }

    @Operation(summary = "Delete a course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Course deleted"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "Course ID") @PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // @Operation(summary = "Search courses by status")
    // @GetMapping("/status/{status}")
    // public ResponseEntity<Page<Course>> getCoursesByStatus(
    //         @Parameter(description = "Course status (PLANIFIEE, EN_COURS, TERMINEE, ANNULEE)") 
    //         @PathVariable String status,
    //         Pageable pageable) {
    //     return ResponseEntity.ok(courseService.findByStatus(status, pageable));
    // }

    @Operation(summary = "Get courses by date range")
    @GetMapping("/date-range")
    public ResponseEntity<Page<Course>> getCoursesByDateRange(
            @Parameter(description = "Start date (YYYY-MM-DD)") @RequestParam LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)") @RequestParam LocalDate endDate,
            Pageable pageable) {
        return ResponseEntity.ok(courseService.findByDateRange(startDate, endDate, pageable));
    }
}

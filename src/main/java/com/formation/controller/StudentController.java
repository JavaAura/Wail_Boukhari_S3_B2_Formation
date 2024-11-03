package com.formation.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.formation.entity.Course;
import com.formation.entity.Student;
import com.formation.exception.DuplicateResourceException;
import com.formation.exception.ExceptionCode;
import com.formation.exception.ResourceInUseException;
import com.formation.exception.ResourceNotFoundException;
import com.formation.exception.ValidationException;
import com.formation.service.CourseService;
import com.formation.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Management", description = "APIs for managing students")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, 
    RequestMethod.DELETE, RequestMethod.OPTIONS})
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;

    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Student created successfully",
            content = @Content(schema = @Schema(implementation = Student.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation failed"),
        @ApiResponse(responseCode = "409", description = "Student with same email already exists")
    })
    @PostMapping
    public ResponseEntity<Student> createStudent(
            @Parameter(description = "Student to create", required = true) 
            @RequestBody(required = true) Student student) {
        if (student == null) {
            throw new ValidationException(ExceptionCode.NULL_REQUEST);
        }
        try {
            return new ResponseEntity<>(studentService.save(student), HttpStatus.CREATED);
        } catch (Exception e) {
            if (e.getMessage().contains("email already exists")) {
                throw new DuplicateResourceException(ExceptionCode.STUDENT_EMAIL_EXISTS, 
                    student.getEmail());
            }
            throw new ValidationException(ExceptionCode.STUDENT_ENROLLMENT_FAILED, e.getMessage());
        }
    }

    @Operation(summary = "Get a student by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @Parameter(description = "ID of the student") 
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(studentService.findById(id));
        } catch (Exception e) {
            throw new ResourceNotFoundException(ExceptionCode.STUDENT_NOT_FOUND, id);
        }
    }

    @Operation(summary = "Get all students with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of students retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No students found")
    })
    @GetMapping
    public ResponseEntity<Page<Student>> getAllStudents(
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "lastName", direction = Sort.Direction.ASC) 
            Pageable pageable) {
        try {
            Page<Student> students = studentService.findAll(pageable);
            return students.hasContent() 
                ? ResponseEntity.ok(students)
                : ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ValidationException(ExceptionCode.INVALID_PAGE, pageable.toString());
        }
    }

    @Operation(summary = "Update a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student updated successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @Parameter(description = "ID of the student") 
            @PathVariable Long id,
            @Parameter(description = "Updated student details") 
            @RequestBody Student student) {
        if (student == null) {
            throw new ValidationException(ExceptionCode.NULL_REQUEST);
        }
        try {
            student.setId(id);
            return ResponseEntity.ok(studentService.update(student));
        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                throw new ResourceNotFoundException(ExceptionCode.STUDENT_NOT_FOUND, id);
            } else if (e.getMessage().contains("email already exists")) {
                throw new DuplicateResourceException(ExceptionCode.STUDENT_EMAIL_EXISTS, 
                    student.getEmail());
            }
            throw new ValidationException(ExceptionCode.STUDENT_ENROLLMENT_FAILED, e.getMessage());
        }
    }

    @Operation(summary = "Delete a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "400", description = "Student cannot be deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Student ID") 
            @PathVariable Long id) {
        try {
            studentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            if (e.getMessage().contains("enrolled in course")) {
                throw new ResourceInUseException(ExceptionCode.STUDENT_IN_COURSE);
            }
            throw new ResourceNotFoundException(ExceptionCode.STUDENT_NOT_FOUND, id);
        }
    }

    @Operation(summary = "Search students by keyword")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No matching students found")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<Student>> searchStudents(
            @Parameter(description = "Search keyword") 
            @RequestParam String keyword,
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable) {
        if (keyword.trim().length() < 2) {
            throw new IllegalArgumentException("Search term must be at least 2 characters long");
        }
        Page<Student> students = studentService.search(keyword, pageable);
        return students.hasContent() 
            ? ResponseEntity.ok(students)
            : ResponseEntity.noContent().build();
            
    }

    @Operation(summary = "Get students by level")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No students found for this level"),
        @ApiResponse(responseCode = "400", description = "Invalid level")
    })
    @GetMapping("/level/{level}")
    public ResponseEntity<Page<Student>> getStudentsByLevel(
            @Parameter(description = "Student level") 
            @PathVariable String level,
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable) {
        try {
            Page<Student> students = studentService.findByLevel(level, pageable);
            return students.hasContent() ? ResponseEntity.ok(students) : 
                ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ValidationException(ExceptionCode.INVALID_LEVEL, level);
        }
    }

    @Operation(summary = "Get students by course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No students found in this course")
    })
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<Student>> getStudentsByCourse(
            @Parameter(description = "Course ID") 
            @PathVariable Long courseId,
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable) {
        try {
            Page<Student> students = studentService.findByCourseId(courseId, pageable);
            return students.hasContent() ? ResponseEntity.ok(students) : 
                ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResourceNotFoundException(ExceptionCode.COURSE_NOT_FOUND, courseId);
        }
    }

    @Operation(summary = "Get students by classroom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No students found in this classroom")
    })
    @GetMapping("/classroom/{classRoomId}")
    public ResponseEntity<Page<Student>> getStudentsByClassRoom(
            @Parameter(description = "Classroom ID") 
            @PathVariable Long classRoomId,
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable) {
        try {
            Page<Student> students = studentService.findByClassRoomId(classRoomId, pageable);
            return students.hasContent() ? ResponseEntity.ok(students) : 
                ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResourceNotFoundException(ExceptionCode.CLASSROOM_NOT_FOUND, classRoomId);
        }
    }

    @Operation(summary = "Get students by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No students found with these names")
    })
    @GetMapping("/name")
    public ResponseEntity<Page<Student>> getStudentsByName(
            @Parameter(description = "Student's last name") 
            @RequestParam String lastName,
            @Parameter(description = "Student's first name") 
            @RequestParam String firstName,
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable) {
        if (lastName == null || lastName.trim().isEmpty() || 
            firstName == null || firstName.trim().isEmpty()) {
            throw new ValidationException(ExceptionCode.STUDENT_INVALID_NAME, 
                lastName + " " + firstName);
        }
        try {
            Page<Student> students = studentService.findByLastNameAndFirstName(
                lastName, firstName, pageable);
            return students.hasContent() 
                ? ResponseEntity.ok(students)
                : ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ValidationException(ExceptionCode.STUDENT_SEARCH_FAILED, 
                lastName + " " + firstName);
        }
    }

    @Operation(summary = "Get courses by date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No courses found for this date range"),
        @ApiResponse(responseCode = "400", description = "Invalid date range")
    })
    @GetMapping("/date-range")
    public ResponseEntity<Page<Course>> getCoursesByDateRange(
            @Parameter(description = "Start date (YYYY-MM-DD)", required = true) 
            @RequestParam LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)", required = true) 
            @RequestParam LocalDate endDate,
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10, sort = "startDate") Pageable pageable) {
        if (startDate.isAfter(endDate)) {
            throw new ValidationException(ExceptionCode.INVALID_DATE_RANGE);
        }
        Page<Course> courses = courseService.findByDateRange(startDate, endDate, pageable);
        return courses.hasContent() ? ResponseEntity.ok(courses) : ResponseEntity.noContent().build();
    }
}
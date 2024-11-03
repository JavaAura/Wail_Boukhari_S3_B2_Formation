package com.formation.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    // Classroom related codes
    CLASSROOM_NOT_FOUND("CLS-404", "Classroom not found with id: %s"),
    CLASSROOM_NUMBER_EXISTS("CLS-409", "Classroom with number '%s' already exists"),
    CLASSROOM_WITH_STUDENTS("CLS-409-S", "Cannot delete classroom with enrolled students"),
    CLASSROOM_WITH_TRAINERS("CLS-409-T", "Cannot delete classroom with assigned trainers"),
    CLASSROOM_CAPACITY_EXCEEDED("CLS-409-C", "Classroom capacity exceeded: current %d, max %d"),
    CLASSROOM_SCHEDULE_CONFLICT("CLS-409-T", "Classroom schedule conflict at requested time"),
    CLASSROOM_MAINTENANCE("CLS-409-M", "Classroom under maintenance until: %s"),
    CLASSROOM_INVALID_SETUP("CLS-400-S", "Invalid classroom setup configuration"),
    CLASSROOM_NOT_EMPTY("CLS-409-E", "Classroom is not empty"),
    CLASSROOM_INVALID_CAPACITY_RANGE("CLS-400-R", "Capacity range is invalid: min %d, max %d"),
    CLASSROOM_NOT_AVAILABLE("CLS-409-A", "Classroom is not available at requested time"),
    CLASSROOM_STATUS_INVALID("CLS-400-T", "Invalid classroom status: %s"),
    CLASSROOM_EQUIPMENT_UNAVAILABLE("CLS-409-Q", "Required equipment not available: %s"),
    CLASSROOM_MAX_OCCUPANCY("CLS-409-O", "Maximum occupancy exceeded: current %d, max %d"),
    
    // Student related codes
    STUDENT_NOT_FOUND("STD-404", "Student not found with id: %s"),
    STUDENT_EMAIL_EXISTS("STD-409", "Student with email '%s' already exists"),
    STUDENT_IN_COURSE("STD-409-C", "Student is already enrolled in course: %s"),
    STUDENT_NOT_IN_CLASSROOM("STD-400-R", "Student must be assigned to a classroom"),
    STUDENT_ENROLLMENT_FAILED("STD-400-E", "Failed to enroll student: %s"),
    INVALID_LEVEL("STD-400-L", "Invalid student level: %s"),
    STUDENT_INVALID_NAME("STD-400-N", "Invalid student name format: %s"),
    STUDENT_INVALID_AGE("STD-400-A", "Student age must be between %d and %d"),
    STUDENT_DUPLICATE_ENROLLMENT("STD-409-D", "Student already enrolled in another course"),
    STUDENT_CLASSROOM_FULL("STD-409-F", "Selected classroom is at full capacity"),
    STUDENT_SEARCH_FAILED("STD-400-S", "Failed to search students: %s"),

    // Course related codes
    COURSE_NOT_FOUND("CRS-404", "Course not found with id: %s"),
    COURSE_TITLE_EXISTS("CRS-409", "Course with title '%s' already exists"),
    COURSE_WITH_STUDENTS("CRS-409-S", "Cannot delete course with enrolled students"),
    COURSE_IN_PROGRESS("CRS-409-P", "Cannot modify course in progress"),
    COURSE_DATES_INVALID("CRS-400-D", "Invalid course dates: %s"),
    COURSE_INVALID_CAPACITY("CRS-400-C", "Course capacity must be between %d and %d"),
    COURSE_SCHEDULE_CONFLICT("CRS-409-S", "Schedule conflict with existing course: %s"),
    COURSE_INVALID_DURATION("CRS-400-T", "Course duration must be between %d and %d days"),
    COURSE_REGISTRATION_CLOSED("CRS-409-R", "Course registration period has ended"),
    
    // Trainer related codes
    TRAINER_NOT_FOUND("TRN-404", "Trainer not found with id: %s"),
    TRAINER_EMAIL_EXISTS("TRN-409", "Trainer with email '%s' already exists"),
    TRAINER_MAX_COURSES("TRN-409-C", "Trainer has reached maximum course load: %s"),
    TRAINER_NOT_AVAILABLE("TRN-409-A", "Trainer is not available for this time slot"),
    
    // Additional validation codes
    INVALID_ROOM_NUMBER("VAL-400-R", "Invalid room number format: %s"),
    INVALID_EQUIPMENT_LIST("VAL-400-E", "Invalid equipment list format"),
    INVALID_MAINTENANCE_DATE("VAL-400-M", "Invalid maintenance date range"),
    INVALID_OCCUPANCY_LIMIT("VAL-400-O", "Occupancy limit must be between %d and %d"),
    INVALID_ROOM_TYPE("VAL-400-T", "Invalid room type: %s"),
    
    // Validation codes
    NULL_REQUEST("VAL-400-N", "Request body cannot be null"),
    INVALID_SEARCH("VAL-400-S", "Search term must be at least %d characters long"),
    INVALID_EMAIL("VAL-400-E", "Invalid email format: %s"),
    INVALID_DATE_RANGE("VAL-400-D", "Start date must be before end date"),
    INVALID_CAPACITY("VAL-400-C", "Capacity must be between %d and %d"),
    INVALID_PAGE("VAL-400-P", "Invalid pagination parameters: %s"),
    INVALID_PHONE_FORMAT("VAL-400-P", "Invalid phone number format: %s"),
    INVALID_TIME_SLOT("VAL-400-T", "Invalid time slot format or range"),
    INVALID_DOCUMENT_FORMAT("VAL-400-D", "Invalid document format: %s"),
    DUPLICATE_ENTRY("VAL-409", "Duplicate entry found for: %s"),
    RESOURCE_LOCKED("VAL-423", "Resource is currently locked: %s");

    private final String code;
    private final String messageTemplate;

    ExceptionCode(String code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }
}
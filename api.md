# API Documentation

## Table of Contents
- [Trainer Management](#trainer-management)
- [Student Management](#student-management)
- [Classroom Management](#classroom-management)
- [Course Management](#course-management)

## Trainer Management

### Create Trainer
**POST** `/api/trainers`

Creates a new trainer in the system.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "specialty": "Java Development",
  "phoneNumber": "+1234567890"
}
```

### Get Trainer by ID
**GET** `/api/trainers/{id}`

### Get All Trainers
**GET** `/api/trainers?page=0&size=10`

### Update Trainer
**PUT** `/api/trainers/{id}`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "specialty": "Java Development",
  "phoneNumber": "+1234567890"
}
```

### Delete Trainer
**DELETE** `/api/trainers/{id}`

### Search Trainers
**GET** `/api/trainers/search?keyword={keyword}&page=0&size=10`

### Get Trainers by Email
**GET** `/api/trainers/email/{email}?page=0&size=10`

### Get Trainers by Specialty
**GET** `/api/trainers/specialty/{specialty}?page=0&size=10`

### Get Trainers by Name
**GET** `/api/trainers/name?lastName={lastName}&firstName={firstName}&page=0&size=10`

### Get Trainers by Classroom
**GET** `/api/trainers/classroom/{classRoomId}?page=0&size=10`

### Get Available Trainers
**GET** `/api/trainers/available?maxCourses={maxCourses}&page=0&size=10`

### Get Trainers Without Courses
**GET** `/api/trainers/without-courses?page=0&size=10`

## Student Management

### Create Student
**POST** `/api/students`

**Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "level": "INTERMEDIATE",
  "dateOfBirth": "2000-01-01"
}
```

### Get Student by ID
**GET** `/api/students/{id}`

### Get All Students
**GET** `/api/students?page=0&size=10`

### Update Student
**PUT** `/api/students/{id}`

**Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "level": "ADVANCED",
  "dateOfBirth": "2000-01-01"
}
```

### Delete Student
**DELETE** `/api/students/{id}`

### Search Students
**GET** `/api/students/search?keyword={keyword}&page=0&size=10`

### Get Students by Level
**GET** `/api/students/level/{level}?page=0&size=10`

### Get Students by Course
**GET** `/api/students/course/{courseId}?page=0&size=10`

### Get Students by Classroom
**GET** `/api/students/classroom/{classRoomId}?page=0&size=10`

### Get Students by Name
**GET** `/api/students/name?lastName={lastName}&firstName={firstName}&page=0&size=10`

## Classroom Management

### Create Classroom
**POST** `/api/classrooms`

**Request Body:**
```json
{
  "name": "Room 101",
  "capacity": 30,
  "building": "Main Building",
  "floor": 1,
  "equipment": ["Projector", "Whiteboard"]
}
```

### Get Classroom by ID
**GET** `/api/classrooms/{id}`

### Get All Classrooms
**GET** `/api/classrooms?page=0&size=10`

### Update Classroom
**PUT** `/api/classrooms/{id}`

**Request Body:**
```json
{
  "name": "Room 101",
  "capacity": 35,
  "building": "Main Building",
  "floor": 1,
  "equipment": ["Projector", "Whiteboard", "Computers"]
}
```

### Delete Classroom
**DELETE** `/api/classrooms/{id}`

### Search Classrooms
**GET** `/api/classrooms/search?keyword={keyword}&page=0&size=10`

### Get Available Rooms
**GET** `/api/classrooms/available?capacity={capacity}&page=0&size=10`

### Get Empty Rooms
**GET** `/api/classrooms/empty?page=0&size=10`

### Get Rooms Without Trainers
**GET** `/api/classrooms/without-trainers?page=0&size=10`

## Course Management

### Create Course
**POST** `/api/courses`

**Request Body:**
```json
{
   "title": "Test Spring Boot Course",
  "level": "Intermediate",
  "prerequisites": "Java Core, Basic Spring",
  "minCapacity": 5,
  "maxCapacity": 20,
  "currentCapacity": 0,
  "startDate": "2024-04-01",
  "endDate": "2024-06-30",
  "status": "PLANNED"
}
```

### Get Course by ID
**GET** `/api/courses/{id}`

### Get All Courses
**GET** `/api/courses?page=0&size=10`

### Update Course
**PUT** `/api/courses/{id}`

**Request Body:**
```json
{
  "title": "Test Spring Boot Course updated",
  "level": "Intermediate",
  "prerequisites": "Java Core, Basic Spring",
  "minCapacity": 5,
  "maxCapacity": 10,
  "currentCapacity": 0,
  "startDate": "2024-04-01",
  "endDate": "2024-06-30",
  "status": "PLANNED"
}
```

### Delete Course
**DELETE** `/api/courses/{id}`

### Get Courses by Date Range
**GET** `/api/courses/date-range?startDate={startDate}&endDate={endDate}&page=0&size=10`
```

This documentation provides a comprehensive overview of all available APIs in your controllers. Each endpoint includes:
- HTTP method and path
- Description where applicable
- Request body examples for POST/PUT operations
- Query parameters where applicable
- Pagination parameters where supported

The JSON examples are structured based on the entity classes referenced in your controllers. You may want to adjust the specific fields based on your actual entity implementations.

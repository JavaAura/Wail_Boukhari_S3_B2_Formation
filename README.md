# Formation Management System

A comprehensive training center management system built with Spring Boot, providing REST APIs for managing courses, students, trainers, and classrooms.

## 🚀 Features

- **Course Management**
  - Create, update, and delete training courses
  - Track course capacity and enrollment
  - Manage course schedules and prerequisites
  - Search courses by various criteria

- **Student Management**
  - Student registration and profile management
  - Track student enrollment in courses
  - Manage student classroom assignments
  - Search students by multiple criteria

- **Trainer Management**
  - Trainer profile management
  - Track trainer specialties and assignments
  - Manage trainer-course relationships
  - Search trainers by expertise

- **Classroom Management**
  - Classroom capacity management
  - Track room availability
  - Manage room assignments
  - Search available rooms

## 🛠 Technology Stack

- **Backend**: Spring Boot 3.x
- **Database**: JPA/Hibernate with H2 (dev) and PostgreSQL (prod)
- **API Documentation**: OpenAPI 3.0 (Swagger)
- **Testing**: JUnit 5, MockMvc
- **Build Tool**: Maven
- **Java Version**: 17

## 📚 API Documentation

The API documentation is available through Swagger UI:
- Local: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

## 🚦 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL (for production)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/formation-management.git
cd formation-management
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

### Configuration

The application uses different profiles for development and production:

- Development (H2 Database):
```properties
spring.profiles.active=dev
```

- Production (PostgreSQL):
```properties
spring.profiles.active=prod
```

## 🔒 Security

- CORS is enabled for all origins in development
- Proper security measures should be implemented for production use

## 🧪 Testing

Run the tests using Maven:
```bash
mvn test
```

## 📝 API Endpoints

### Trainer Management
- `POST /api/trainers` - Create a new trainer
- `GET /api/trainers/{id}` - Get trainer by ID
- `GET /api/trainers` - Get all trainers (paginated)
- `PUT /api/trainers/{id}` - Update a trainer
- `DELETE /api/trainers/{id}` - Delete a trainer

### Student Management
- `POST /api/students` - Create a new student
- `GET /api/students/{id}` - Get student by ID
- `GET /api/students` - Get all students (paginated)
- `PUT /api/students/{id}` - Update a student
- `DELETE /api/students/{id}` - Delete a student

### Course Management
- `POST /api/courses` - Create a new course
- `GET /api/courses/{id}` - Get course by ID
- `GET /api/courses` - Get all courses (paginated)
- `PUT /api/courses/{id}` - Update a course
- `DELETE /api/courses/{id}` - Delete a course

### Classroom Management
- `POST /api/classrooms` - Create a new classroom
- `GET /api/classrooms/{id}` - Get classroom by ID
- `GET /api/classrooms` - Get all classrooms (paginated)
- `PUT /api/classrooms/{id}` - Update a classroom
- `DELETE /api/classrooms/{id}` - Delete a classroom

## 📦 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/formation/
│   │       ├── controller/    # REST controllers
│   │       ├── entity/        # Domain models
│   │       ├── repository/    # Data access layer
│   │       ├── service/       # Business logic
│   │       ├── config/        # Configuration classes
│   │       └── exception/     # Custom exceptions
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/formation/
            └── controller/    # Integration tests
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- Wail Boukhari - Initial work

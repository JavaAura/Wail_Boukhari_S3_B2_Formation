package com.formation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.formation.entity.enums.CourseStatus;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"trainer", "students"})
@EqualsAndHashCode(of = {"id", "title"})
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String title;
    
    @NotBlank(message = "Level is required")
    @Size(min = 2, max = 20, message = "Level must be between 2 and 20 characters")
    @Column(nullable = false, length = 20)
    private String level;
    
    @Size(max = 500, message = "Prerequisites must not exceed 500 characters")
    @Column(length = 500)
    private String prerequisites;
    
    @Min(value = 1, message = "Minimum capacity must be at least 1")
    @Column(nullable = false)
    private int minCapacity;
    
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    @Column(nullable = false)
    private int maxCapacity;
    
    @Builder.Default
    @Column(nullable = false)
    private int currentCapacity = 0;
    
    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    @Column(nullable = false, length = 20)
    private CourseStatus status;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
    
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Student> students = new HashSet<>();
    
    public boolean isAvailable() {
        return currentCapacity < maxCapacity;
    }
    
    public boolean addStudent(Student student) {
        if (isAvailable()) {
            boolean added = students.add(student);
            if (added) {
                currentCapacity++;
            }
            return added;
        }
        return false;
    }
    
    public boolean removeStudent(Student student) {
        boolean removed = students.remove(student);
        if (removed) {
            currentCapacity--;
        }
        return removed;
    }
}

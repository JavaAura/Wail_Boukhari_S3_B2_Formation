package com.formation.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"students", "trainers"})
@EqualsAndHashCode(of = {"id", "roomNumber"})
@Table(name = "classrooms")
public class ClassRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Room number is required")
    @Size(min = 2, max = 20, message = "Room number must be between 2 and 20 characters")
    @Column(nullable = false, unique = true, length = 20)
    private String roomNumber;
    
    @Min(value = 0, message = "Current capacity cannot be negative")
    @Column(nullable = false)
    @Builder.Default
    private Integer currentCapacity = 0;
    
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    @Column(nullable = false)
    @Builder.Default
    private Integer maxCapacity = 30;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "classRoom", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Student> students = new HashSet<>();
    
    @OneToMany(mappedBy = "classRoom", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Trainer> trainers = new HashSet<>();
    
    public boolean isAvailable() {
        return currentCapacity < maxCapacity;
    }
    
    public boolean isEmpty() {
        return students.isEmpty() && trainers.isEmpty();
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

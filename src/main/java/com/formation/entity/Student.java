package com.formation.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"course", "classRoom"})
@EqualsAndHashCode(of = {"id", "email"})
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-']+$", message = "Last name can only contain letters, spaces, hyphens and apostrophes")
    @Column(nullable = false, length = 50)
    private String lastName;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-']+$", message = "First name can only contain letters, spaces, hyphens and apostrophes")
    @Column(nullable = false, length = 50)
    private String firstName;
    
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @NotBlank(message = "Level is required")
    @Size(min = 2, max = 20, message = "Level must be between 2 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Level can only contain letters, numbers, spaces and hyphens")
    @Column(nullable = false, length = 20)
    private String level;
    
    @CreationTimestamp
    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private ClassRoom classRoom;
}

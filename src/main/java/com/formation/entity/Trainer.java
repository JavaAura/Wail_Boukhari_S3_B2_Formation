package com.formation.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"courses", "classRoom"})
@EqualsAndHashCode(of = {"id", "email"})
@Table(name = "trainers")
public class Trainer {
    
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
    
    @NotBlank(message = "Specialty is required")
    @Size(min = 2, max = 50, message = "Specialty must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-']+$", message = "Specialty can only contain letters, spaces, hyphens and apostrophes")
    @Column(nullable = false, length = 50)
    private String specialty;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Course> courses = new HashSet<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private ClassRoom classRoom;
}

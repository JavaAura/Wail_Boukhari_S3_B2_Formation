package com.formation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.formation.entity.Student;

public interface StudentService {
    Student save(Student student);
    Student findById(Long id);
    Page<Student> findAll(Pageable pageable);
    Student update(Student student);
    void delete(Long id);
    Page<Student> search(String keyword, Pageable pageable);
    Page<Student> findByLevel(String level, Pageable pageable);
    Page<Student> findByCourseId(Long courseId, Pageable pageable);
    Page<Student> findByClassRoomId(Long classRoomId, Pageable pageable);
    Page<Student> findByLastNameAndFirstName(String lastName, String firstName, Pageable pageable);
}

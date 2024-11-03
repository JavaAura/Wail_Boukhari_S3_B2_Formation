package com.formation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.formation.entity.Student;
import com.formation.repository.StudentRepository;
import com.formation.service.StudentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Service
@Transactional
@Validated
public class StudentServiceImpl implements StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Override
    public Student save(@Valid @NotNull Student student) {
        return studentRepository.save(student);
    }
    
    @Override
    public Student findById(@NotNull Long id) {
        return studentRepository.findById(id)
            .orElse(null);
    }
    
    @Override
    public Page<Student> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
    
    @Override
    public Student update(@Valid @NotNull Student student) {
        return studentRepository.save(student);
    }
    
    @Override
    public void delete(@NotNull Long id) {
        studentRepository.deleteById(id);
    }
    
    @Override
    public Page<Student> search(String keyword, Pageable pageable) {
        return studentRepository.search(keyword, pageable);
    }
    
    @Override
    public Page<Student> findByLevel(String level, Pageable pageable) {
        return studentRepository.findByLevel(level, pageable);
    }
    
    @Override
    public Page<Student> findByCourseId(Long courseId, Pageable pageable) {
        return studentRepository.findByCourseId(courseId, pageable);
    }
    
    @Override
    public Page<Student> findByClassRoomId(Long classRoomId, Pageable pageable) {
        return studentRepository.findByClassRoomId(classRoomId, pageable);
    }
    
    @Override
    public Page<Student> findByLastNameAndFirstName(String lastName, String firstName, Pageable pageable) {
        return studentRepository.findByLastNameAndFirstName(lastName, firstName, pageable);
    }
}
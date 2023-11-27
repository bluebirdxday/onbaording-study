package com.fastcampus.springboot.cache.service;

import com.fastcampus.springboot.cache.domain.Student;
import com.fastcampus.springboot.cache.repository.StudentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public void printStudent(String name){
        Student student = studentRepository.getStudent(name);
        System.out.println("찾는 학생 : " + student);
    }

    @PostConstruct
    public void init(){
        studentRepository.enroll("jack", 15, Student.Grade.B);
    }
}

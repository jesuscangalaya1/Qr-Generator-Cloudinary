package com.qrgenerator.controller;

import com.cloudinary.Cloudinary;
import com.google.zxing.WriterException;
import com.qrgenerator.dto.StudentRequest;
import com.qrgenerator.dto.StudentResponse;
import com.qrgenerator.entity.Student;
import com.qrgenerator.service.impl.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<StudentResponse> addStudent(@RequestBody StudentRequest student) throws IOException, WriterException {
        return new ResponseEntity<>(studentService.addStudent(student), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(studentService.findById(id), HttpStatus.OK);
    }


}

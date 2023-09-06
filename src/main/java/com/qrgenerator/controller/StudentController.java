package com.qrgenerator.controller;

import com.cloudinary.Cloudinary;
import com.google.zxing.WriterException;
import com.qrgenerator.entity.Student;
import com.qrgenerator.service.StudentService;
import com.qrgenerator.util.QRCodeGenerator;
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
    private final Cloudinary cloudinary;

    @GetMapping
    public ResponseEntity<List<Student>> getStudents() throws IOException, WriterException {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student){
        return new ResponseEntity<>(studentService.addStudent(student), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable("id") Long id){
        return new ResponseEntity<>(studentService.findById(id), HttpStatus.OK);
    }


}

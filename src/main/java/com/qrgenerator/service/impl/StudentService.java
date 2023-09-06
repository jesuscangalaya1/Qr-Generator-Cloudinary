package com.qrgenerator.service.impl;

import com.google.zxing.WriterException;
import com.qrgenerator.dto.StudentRequest;
import com.qrgenerator.dto.StudentResponse;
import com.qrgenerator.entity.Student;
import com.qrgenerator.repository.StudentRepository;
import com.qrgenerator.service.QrGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final QrGeneratorService qrGeneratorService;

    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::buildStudentResponse)
                .toList();
    }


    public StudentResponse addStudent(StudentRequest studentRequest) throws IOException, WriterException {
        // Crea una entidad Student a partir de los datos del DTO StudentRequest
        Student student = buildStudentFromRequest(studentRequest);

        // Guarda al estudiante en la base de datos
        Student savedStudent = studentRepository.save(student);

        // Genera y almacena el código QR para el estudiante (aquí se utiliza el tipo StudentResponse)
        String qrCodeImageUrl = qrGeneratorService.generateAndUploadQRCode(buildStudentResponse(savedStudent));

        // Actualiza el estudiante con la URL del código QR
        savedStudent.setUrl(qrCodeImageUrl);
        studentRepository.save(savedStudent);

        // Crea un DTO de respuesta con los datos actualizados
        return buildStudentResponse(savedStudent);
    }

    public StudentResponse findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return buildStudentResponse(student);
    }




    // Mapear dtos a entidaes ...
    private Student buildStudentFromRequest(StudentRequest studentRequest) {
        return Student.builder()
                .firstName(studentRequest.getFirstName())
                .lastName(studentRequest.getLastName())
                .email(studentRequest.getEmail())
                .mobile(studentRequest.getMobile())
                .build();
    }

    private StudentResponse buildStudentResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .mobile(student.getMobile())
                .url(student.getUrl())
                .build();
    }
}
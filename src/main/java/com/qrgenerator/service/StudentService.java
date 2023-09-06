package com.qrgenerator.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.qrgenerator.entity.Student;
import com.qrgenerator.repository.StudentRepository;
import com.qrgenerator.util.QRCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final Cloud cloud; // Inyecta la instancia de Cloud


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student addStudent(Student student) {
        // Guarda al estudiante en la base de datos
        Student savedStudent = studentRepository.save(student);

        try {
            // Genera el código QR y lo almacena en Cloudinary
            String qrCodeImageUrl = generateAndUploadQRCode(savedStudent);
            savedStudent.setUrl(qrCodeImageUrl);

            // Actualiza el estudiante con la URL del código QR
            studentRepository.save(savedStudent);
        } catch (IOException | WriterException e) {
            // Maneja excepciones si ocurre algún error durante la generación o carga
            e.printStackTrace();
        }

        return savedStudent;
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    private String generateAndUploadQRCode(Student student) throws IOException, WriterException {
        // Genera el contenido del código QR basado en los datos del estudiante
        String qrCodeContent = "ID: " + student.getId() + "\n" +
                               "Firstname: " + student.getFirstName() + "\n" +
                               "Lastname: " + student.getLastName() + "\n" +
                               "Email: " + student.getEmail() + "\n" +
                               "Mobile: " + student.getMobile();

        // Genera el código QR utilizando ZXing
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generateQRCode(qrCodeContent, outputStream);

        // Carga el flujo de bytes en Cloudinary
        Map result = cloud.upload(new ByteArrayInputStream(outputStream.toByteArray()));

        // Puedes obtener la URL de la imagen generada en Cloudinary
        return (String) result.get("url");
    }

    private void generateQRCode(String qrCodeContent, ByteArrayOutputStream outputStream) throws IOException, WriterException {
        com.google.zxing.qrcode.QRCodeWriter qrCodeWriter = new com.google.zxing.qrcode.QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, 400, 400);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
    }


}
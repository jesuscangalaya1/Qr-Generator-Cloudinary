package com.qrgenerator.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.qrgenerator.dto.StudentResponse;
import com.qrgenerator.service.QrGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrGeneratorServiceImpl implements QrGeneratorService {

    private final CloudinaryService cloudinaryService;

    /**
     * Genera un código QR a partir de los datos de un estudiante y lo carga en Cloudinary.
     *
     * @param student El estudiante cuyos datos se utilizarán para generar el código QR.
     * @return La URL de la imagen del código QR generada en Cloudinary.
     * @throws IOException     Si ocurre un error durante la generación o carga del código QR.
     * @throws WriterException Si ocurre un error al generar el código QR.
     */
    @Override
    public String generateAndUploadQRCode(StudentResponse student) throws IOException, WriterException {
        String qrCodeContent = generateQRCodeContent(student);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            generateQRCode(qrCodeContent, outputStream);

            // Carga el flujo de bytes en Cloudinary
            Map result = cloudinaryService.upload(new ByteArrayInputStream(outputStream.toByteArray()));

            // Puedes obtener la URL de la imagen generada en Cloudinary
            return (String) result.get("url");
        } catch (IOException e) {
            // Maneja las excepciones aquí (puede registrar o lanzar una excepción personalizada)
            throw new IOException("Error al generar y cargar el código QR", e);
        }
    }

    /**
     * Genera el contenido del código QR utilizando los datos del estudiante.
     *
     * @param student El estudiante cuyos datos se utilizarán para el contenido del código QR.
     * @return El contenido del código QR como una cadena de texto.
     */
    private String generateQRCodeContent(StudentResponse student) {
        return "ID: " + student.getId() + "\n" +
               "Firstname: " + student.getFirstName() + "\n" +
               "Lastname: " + student.getLastName() + "\n" +
               "Email: " + student.getEmail() + "\n" +
               "Mobile: " + student.getMobile();
    }

    /**
     * Genera el código QR a partir del contenido proporcionado y lo escribe en un flujo de bytes.
     *
     * @param qrCodeContent El contenido que se utilizará para generar el código QR.
     * @param outputStream  El flujo de bytes en el que se escribirá el código QR generado.
     * @throws IOException     Si ocurre un error al escribir el código QR en el flujo de bytes.
     * @throws WriterException Si ocurre un error al generar el código QR.
     */
    private void generateQRCode(String qrCodeContent, OutputStream outputStream) throws IOException, WriterException {
        com.google.zxing.qrcode.QRCodeWriter qrCodeWriter = new com.google.zxing.qrcode.QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, 400, 400);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
    }

}

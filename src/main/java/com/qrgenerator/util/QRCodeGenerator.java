package com.qrgenerator.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qrgenerator.entity.Student;
import com.qrgenerator.service.Cloud;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

public class QRCodeGenerator {


    public void generateQRCode(Student student) throws WriterException, IOException {
        String qrCodePath = "D:\\blog-posts\\QRCode\\";
        String qrCodeName = qrCodePath+student.getFirstName()+student.getId()+"-QRCODE.png";
        var qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(
                "ID: "+student.getId()+ "\n"+
                "Firstname: "+student.getFirstName()+ "\n"+
                "Lastname: "+student.getLastName()+ "\n"+
                "Email: "+student.getEmail()+ "\n" +
                "Mobile: "+student.getMobile(), BarcodeFormat.QR_CODE, 400, 400);
        //Map uploadResult = cloud.upload(qrCodeName);
        Path path = FileSystems.getDefault().getPath(qrCodeName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }




}

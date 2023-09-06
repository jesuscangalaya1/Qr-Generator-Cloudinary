package com.qrgenerator.service;

import com.google.zxing.WriterException;
import com.qrgenerator.dto.StudentResponse;

import java.io.IOException;

public interface QrGeneratorService {

    String generateAndUploadQRCode(StudentResponse student) throws IOException, WriterException;


}

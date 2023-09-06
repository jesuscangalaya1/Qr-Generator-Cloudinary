package com.qrgenerator.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map upload(ByteArrayInputStream byteArrayInputStream) throws IOException {
        File file = convert(byteArrayInputStream);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        file.delete();
        return result;
    }

    public File convert(ByteArrayInputStream byteArrayInputStream) throws IOException {
        byte[] bytes = new byte[byteArrayInputStream.available()];
        byteArrayInputStream.read(bytes);
        File file = File.createTempFile("temp", null); // Crea un archivo temporal
        try (FileOutputStream fo = new FileOutputStream(file)) {
            fo.write(bytes);
        }
        return file;
    }

   /* private Map<String, String> valuesMap = new HashMap<>();

    public CloudinaryService() {
        valuesMap.put("cloud_name", "demsg98l6");
        valuesMap.put("api_key", "392713875887788");
        valuesMap.put("api_secret", "MI1Kf6hSWRATNmysclH-aDB84N4");
        cloudinary = new Cloudinary(valuesMap);
    }

    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        file.delete();
        return result;    }

    public File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }*/

}


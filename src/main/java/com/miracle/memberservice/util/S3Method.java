package com.miracle.memberservice.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class S3Method {

    private final AmazonS3Client amazonS3Client;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void uploadFile(MultipartFile file, String type, String name) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, type + "/" + name, file.getInputStream(), objectMetadata);
    }

    public void deleteFile(String type, String name) {
        amazonS3Client.deleteObject(bucket, type + "/" + name);
    }

    public String getUrl(String type, String name) {
        return amazonS3.getUrl(bucket, type + "/" + name).toString();
    }

    public void getFile(String type, String name) {
        amazonS3.getObject(bucket, type + "/" + name);
    }

    public boolean isExistFile(String type, String name) {
        return amazonS3Client.doesObjectExist(bucket, type + "/" + name);
    }
}

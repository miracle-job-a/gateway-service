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

    public void uploadFile(MultipartFile file, String type, Long id) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, type + "/" + id, file.getInputStream(), objectMetadata);
    }

    public void deleteFile(String type, Long id) {
        amazonS3Client.deleteObject(bucket, type + "/" + id);
    }

    public String getUrl(String type, Long id) {
        return amazonS3.getUrl(bucket, type + "/" + id).toString();
    }
}

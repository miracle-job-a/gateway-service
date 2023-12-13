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

    public void uploadFileResume(MultipartFile file, Long id) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, Const.RequestHeader.RESUME + "/" + id, file.getInputStream(), objectMetadata);
    }

    public void uploadCompanyFile(MultipartFile file, String bno) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, Const.RequestHeader.COMPANY + "/" + bno, file.getInputStream(), objectMetadata);
    }

    public void deleteFileResume(Long id) {
        amazonS3Client.deleteObject(bucket, Const.RequestHeader.RESUME + "/" + id);
    }

    public void deleteFileCompany(String bno) {
        amazonS3Client.deleteObject(bucket, Const.RequestHeader.COMPANY + "/" + bno);
    }

    public String getUrlResume(Long id) {
        return amazonS3.getUrl(bucket, Const.RequestHeader.RESUME + "/" + id).toString();
    }

    public String getUrlCompany(String bno) {
        return amazonS3.getUrl(bucket, Const.RequestHeader.COMPANY + "/" + bno).toString();
    }
}

package com.duckervn.streamservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    @Value("${s3.bucketName}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    public Resource getResource(String objectKey) {
        S3Object s3Object = amazonS3.getObject(bucketName, objectKey);
        byte[] content;
        try {
            content = IOUtils.toByteArray(s3Object.getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ByteArrayResource(content);
    }
}

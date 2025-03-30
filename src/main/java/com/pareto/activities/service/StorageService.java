package com.pareto.activities.service;

import com.pareto.activities.config.MinioConfig;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String getObjectUrl(String bucket, String objectName) {
        try {
            return  minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucket)
                            .object(objectName)
                            .expiry(minioConfig.getPresignedUrlExpiry(), TimeUnit.MINUTES)
                            .build());
        } catch (Exception e) {
            //TODO Business exc
            throw new RuntimeException(e);
        }
    }
}

package com.pareto.activities.service.impl;

import com.pareto.activities.config.MinioConfig;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.service.IStorageService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioStorageService implements IStorageService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String getObjectUrl(
            String bucket,
            String objectName,
            Method method
    ) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(method)
                            .bucket(bucket)
                            .object(objectName)
                            .expiry(minioConfig.getPresignedUrlExpiry(), TimeUnit.MINUTES)
                            .build());
        } catch (
                Exception e) {
            throw new BusinessException(BusinessStatus.MINIO_ERROR, HttpStatus.NO_CONTENT);
        }
    }
}

package com.pareto.activities.service;

import com.pareto.activities.repository.EventRepository;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final MinioClient minioClient;
    private final DataSource dataSource;
    private final EventRepository eventRepository;

    @Value("${minio.bucket.name.event-background}")
    private String bucketName;

    @Value("${minio.presigned-url.expiry:5}")
    private int presignedUrlExpiryMinutes;

    public String generatePresignedUploadUrl(String objectName) throws Exception {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(presignedUrlExpiryMinutes, TimeUnit.MINUTES)
                            .build());
        } catch (MinioException e) {
            throw new RuntimeException("Error generating pre-signed URL", e);
        }
    }

//    private String generateUniqueFileName(String fileExtension) throws Exception {
//        String fileName = String.valueOf(sequenceRepository.getNextSequenceValue("minio_object_sequence"));
//        if (fileExtension != null && !fileExtension.isEmpty()) {
//            if (!fileExtension.startsWith(".")) {
//                fileName += ".";
//            }
//            fileName += fileExtension;
//        }
//        return fileName;
//    }
}

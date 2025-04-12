package com.pareto.activities.service;

import io.minio.http.Method;

public interface IStorageService {
    String getObjectUrl(
            String bucket,
            String objectName,
            Method method
    );
    String generateUniqueFileName();
}

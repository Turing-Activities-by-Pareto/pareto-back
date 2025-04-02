package com.pareto.activities;

import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.service.impl.MinioStorageService;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TestHash {

    @Autowired
    private MinioStorageService minio;

    @Autowired
    private MinioClient minioClient;

    @Test
    @Disabled
    void testMinio() {

        Exception ex = assertThrows(
                ErrorResponseException.class,
                () -> minioClient.statObject(
                        StatObjectArgs
                                .builder()
                                .bucket("event-background")
                                .object("nonexistent-file")
                                .build()
                )
        );

        Assertions.assertEquals(
                "dfafa",
                ex.getMessage()
        );
    }

    @Test
    @Disabled
    void testHashUtil() {
        String input = "Hello, World!";
        String actualHash = HashUtil.hashString(input);

        Assertions.assertEquals(
                input,
                actualHash
        );
    }

    public class HashUtil {
        public static String hashString(String input) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
                return bytesToHex(hashBytes);
            } catch (
                    NoSuchAlgorithmException e) {
                throw new RuntimeException(
                        "SHA-256 Algorithm not available",
                        e
                );
            }
        }

        private static String bytesToHex(byte[] bytes) {
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                hexString.append(String.format(
                        "%02x",
                        b
                ));
            }
            return hexString.toString();
        }
    }
}
package ru.panfio.telescreen.config;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String ENDPOINT;

    @Value("${minio.access.key}")
    private String ACCESS_KEY;

    @Value("${minio.secret.key}")
    private String SECRET_KEY;

    @Bean
    public MinioClient minioClient() throws MinioException {
        try {
            return new MinioClient(
                    ENDPOINT,
                    ACCESS_KEY,
                    SECRET_KEY);
        } catch(MinioException e) {
            System.out.println("Error occurred: " + e);
            throw new RuntimeException("Minio isn't available");
        }
    }
}

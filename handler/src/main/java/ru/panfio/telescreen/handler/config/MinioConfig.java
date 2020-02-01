package ru.panfio.telescreen.handler.config;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    /**
     * Minio connection.
     * @return MinioClient
     * @throws MinioException if error
     */
    @Bean
    public MinioClient minioClient() throws MinioException {
        try {
            return new MinioClient(
                    endpoint,
                    accessKey,
                    secretKey);
        } catch (MinioException e) {
            log.error("Error occurred: " + e);
            throw new RuntimeException("Minio isn't available");
        }
    }
}

package com.levelupfit.mainbackend.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Data
public class MinioConfig {

    @Value("${MINIO_ACCESS_KEY}")
    private String MinIO_Access_key;
    
    @Value("${MINIO_SECRET_KEY}")
    private String MinIO_Secret_key;
    
    @Value("${MINIO_URL}") //이건 배포하면서 수정해야함
    private String MinIO_URL;
    

    private MinioClient minioClient;


    public MinioClient initMinioClient(){
        if (MinIO_Access_key == null || MinIO_Secret_key == null || MinIO_URL == null) {
            throw new IllegalStateException("MinIO 환경변수가 누락되었습니다.");
        }
        minioClient = MinioClient.builder()
                .endpoint(MinIO_URL)
                .credentials(MinIO_Access_key,MinIO_Secret_key)
                .build();
        try{
            minioClient.ignoreCertCheck();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return minioClient;
    }
}

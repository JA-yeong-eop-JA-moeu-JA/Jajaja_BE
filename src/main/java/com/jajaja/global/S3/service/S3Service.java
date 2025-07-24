package com.jajaja.global.S3.service;

import com.jajaja.global.S3.dto.request.PresignedUploadListRequestDto;
import com.jajaja.global.S3.dto.request.PresignedUploadRequestDto;
import com.jajaja.global.S3.dto.response.PresignedUrlUploadListResponseDto;
import com.jajaja.global.S3.dto.response.PresignedUrlUploadResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PresignedUrlUploadResponseDto getPresignedUrlToUpload(PresignedUploadRequestDto presignedUploadRequest) {

        Date expiration = new Date();
        long expTime = expiration.getTime();
        expTime += TimeUnit.MINUTES.toMillis(3); // 3분
        expiration.setTime(expTime);

        String keyName = UUID.randomUUID() + "_" + presignedUploadRequest.getFileName();

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, keyName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        String key = generatePresignedUrlRequest.getKey();

        return PresignedUrlUploadResponseDto.builder()
                .url(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
                .keyName(key)
                .build();
    }

    public PresignedUrlUploadListResponseDto getPresignedUrlToUploadList(PresignedUploadListRequestDto presignedUploadListRequest) {

        Date expiration = new Date();
        long expTime = expiration.getTime();
        expTime += TimeUnit.MINUTES.toMillis(3); // 3분
        expiration.setTime(expTime);

        List<PresignedUrlUploadResponseDto> responses = presignedUploadListRequest.getFileNameList().stream()
                .map(oldKeyName -> {
                    String keyName = UUID.randomUUID() + "_" + oldKeyName;

                    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, keyName)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);

                    String key = generatePresignedUrlRequest.getKey();

                    return PresignedUrlUploadResponseDto.builder()
                            .url(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
                            .keyName(key)
                            .build();
                })
                .collect(Collectors.toList());

        return PresignedUrlUploadListResponseDto.builder().presignedUrlUploadResponses(responses).build();
    }

    public boolean doesKeyExist(String keyName) {
        return amazonS3.doesObjectExist(bucket, keyName);
    }

    public String generateStaticUrl(String keyName) {
        return "https://" + bucket + ".s3." + amazonS3.getRegionName() + ".amazonaws.com/" + keyName;
    }
}

package com.jajaja.global.S3.controller;

import com.jajaja.global.S3.dto.request.PresignedUploadRequest;
import com.jajaja.global.S3.dto.response.PresignedUrlUploadResponse;
import com.jajaja.global.S3.service.S3Service;
import com.jajaja.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(
            summary = "업로드용 presigned URL 생성 | by 지지/이지희",
            description = "업로드를 위한 presigned URL을 생성합니다.")
    @PostMapping("/presigned/upload")
    public ApiResponse<PresignedUrlUploadResponse> getPresignedUrlToUpload(@RequestBody PresignedUploadRequest presignedUploadRequest) {
        PresignedUrlUploadResponse presignedUrlToUpload = s3Service.getPresignedUrlToUpload(presignedUploadRequest);
        return ApiResponse.onSuccess(presignedUrlToUpload);
    }
}

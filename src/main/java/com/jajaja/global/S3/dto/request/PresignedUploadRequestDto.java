package com.jajaja.global.S3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "단일 Presigned URL을 요청하는 request")
public class PresignedUploadRequestDto {

    @Schema(description = "업로드할 파일 이름 (예: profile.jpg)")
    private String fileName;
}

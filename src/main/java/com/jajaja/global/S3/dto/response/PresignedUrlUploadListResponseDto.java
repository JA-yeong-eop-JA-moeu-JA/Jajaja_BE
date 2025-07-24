package com.jajaja.global.S3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "여러 개의 Presigned URL에 대한 응답")
public class PresignedUrlUploadListResponseDto {

    List<PresignedUrlUploadResponseDto> presignedUrlUploadResponses;
}

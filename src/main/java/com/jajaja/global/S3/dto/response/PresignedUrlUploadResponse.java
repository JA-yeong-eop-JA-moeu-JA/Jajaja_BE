package com.jajaja.global.S3.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "Presigned URL에 대한 응답")
public class PresignedUrlUploadResponse {

    @Schema(description = "S3에 PUT 요청할 수 있는 Presigned URL")
    private String url;

    @Schema(description = "S3 객체 key 값")
    private String keyName;
}

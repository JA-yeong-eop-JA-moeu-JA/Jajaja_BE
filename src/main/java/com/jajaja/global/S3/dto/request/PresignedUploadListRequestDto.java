package com.jajaja.global.S3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "여러 개 Presigned URL을 요청하는 request")
public class PresignedUploadListRequestDto {

    private List<String> fileNameList;
}

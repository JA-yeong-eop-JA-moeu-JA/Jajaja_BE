package com.jajaja.domain.member.dto.request;

import jakarta.validation.constraints.Pattern;

public record MemberProfileUpdateRequest(
    @Pattern(regexp = "^[가-힣]{2,4}$", message = "이름은 한글로 2~4자 사이여야 합니다.")
    String name,
    @Pattern(regexp = "^(\\d{10}|\\d{11}|\\d{12})$", message = "전화번호는 숫자만 허용하며, 10~12자리여야 합니다.")
    String phone,
    String profileKeyName
) {
}

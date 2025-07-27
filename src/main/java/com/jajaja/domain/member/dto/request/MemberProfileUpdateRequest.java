package com.jajaja.domain.member.dto.request;

public record MemberProfileUpdateRequest(
    String name,
    String phone,
    String profileKeyName
) {
}

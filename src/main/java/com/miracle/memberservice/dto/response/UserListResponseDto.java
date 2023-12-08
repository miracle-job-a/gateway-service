package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class UserListResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final String address;
    private final String joinDate;

    @Builder
    public UserListResponseDto(Object id, Object email, Object name, Object address, Object createdAt) {
        this.id = Long.parseLong(String.valueOf(id));
        this.email = String.valueOf(email);
        this.name = String.valueOf(name);
        this.address = String.valueOf(address);
        this.joinDate = String.valueOf(createdAt);
    }
}
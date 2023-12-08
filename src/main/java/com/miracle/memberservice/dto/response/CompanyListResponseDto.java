package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class CompanyListResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private final Integer employeeNum;
    private final String bno;
    private final Boolean status;
    private final Boolean approveStatus;
    private final String createdAt;

    @Builder
    public CompanyListResponseDto(Object id, Object name, Object email, Object employeeNum, Object bno, Object status, Object approveStatus, Object createdAt) {
        this.id = Long.parseLong(id.toString());
        this.name = String.valueOf(name);
        this.email = String.valueOf(email);
        this.employeeNum = (Integer) employeeNum;
        this.bno = String.valueOf(bno);
        this.status = (Boolean) status;
        this.approveStatus = (Boolean) approveStatus;
        this.createdAt = String.valueOf(createdAt);
    }
}

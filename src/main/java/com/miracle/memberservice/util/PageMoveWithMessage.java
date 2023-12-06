package com.miracle.memberservice.util;

import com.miracle.memberservice.dto.response.CompanyLoginResponseDto;
import com.miracle.memberservice.dto.response.UserLoginResponseDto;
import lombok.Getter;

@Getter
public class PageMoveWithMessage {
    private final String pageName;
    private final String errorMessage;
    private final Long id;
    private final String email;
    private final String nameOrBno;
    private final Object data;

    //메세지
    public PageMoveWithMessage(String pageName, String errorMessage) {
        this.pageName = pageName;
        this.errorMessage = errorMessage;
        this.id = null;
        this.email = null;
        this.nameOrBno = null;
        this.data = null;
    }

    public PageMoveWithMessage(String pageName, String errorMessage, Object data) {
        this.pageName = pageName;
        this.errorMessage = errorMessage;
        this.id = null;
        this.email = null;
        this.nameOrBno = null;
        this.data = data;
    }

    //id만 전달

    public PageMoveWithMessage(String pageName, Long id) {
        this.pageName = pageName;
        this.errorMessage = null;
        this.id = id;
        this.email = null;
        this.nameOrBno = null;
        this.data = null;
    }

    public PageMoveWithMessage(String pageName, Long id, String email, String nameOrBno) {
        this.pageName = pageName;
        this.errorMessage = null;
        this.id = id;
        this.email = email;
        this.nameOrBno = nameOrBno;
        this.data = null;
    }

    public PageMoveWithMessage(String pageName) {
        this.pageName = pageName;
        this.errorMessage = null;
        this.id = null;
        this.email = null;
        this.nameOrBno = null;
        this.data = null;
    }

    public PageMoveWithMessage(String pageName, CompanyLoginResponseDto dto) {
        this.pageName = pageName;
        this.errorMessage = null;
        this.id = dto.getId();
        this.email = dto.getEmail();
        this.nameOrBno = dto.getBno();
        this.data = null;
    }

    public PageMoveWithMessage(String pageName, UserLoginResponseDto dto) {
        this.pageName = pageName;
        this.errorMessage = null;
        this.id = dto.getId();
        this.email = dto.getEmail();
        this.nameOrBno = dto.getName();
        this.data = null;
    }

    public PageMoveWithMessage(String pageName, Object dto){
        this.pageName = pageName;
        this.errorMessage = null;
        this.id = null;
        this.email = null;
        this.nameOrBno = null;
        this.data = dto;
    }
}

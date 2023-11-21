package com.miracle.memberservice.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class PageMoveWithMessage {
    private final String pageName;
    private final String errorMessage;
    private final Long id;
    private final String email;
    private final String nameOrBno;

    //메세지
    public PageMoveWithMessage(String pageName, String errorMessage) {
        this.pageName = pageName;
        this.errorMessage = errorMessage;
        this.id = null;
        this.email = null;
        this.nameOrBno = null;
    }

    //id만 전달

    public PageMoveWithMessage(String pageName, Long id) {
        this.pageName = pageName;
        this.errorMessage = null;
        this.id = id;
        this.email = null;
        this.nameOrBno = null;
    }

    public PageMoveWithMessage(String pageName, Long id, String email, String nameOrBno) {
        this.pageName = pageName;
        this.errorMessage = null;
        this.id = id;
        this.email = email;
        this.nameOrBno = nameOrBno;
    }

    public PageMoveWithMessage(String pageName) {
        this.pageName = pageName;
        this.errorMessage = null;
        this.id = null;
        this.email = null;
        this.nameOrBno = null;
    }
}

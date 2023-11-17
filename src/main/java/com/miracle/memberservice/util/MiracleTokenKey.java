package com.miracle.memberservice.util;

import lombok.Getter;

import javax.servlet.http.HttpSession;

@Getter
public class MiracleTokenKey {
    private static final String PRIVATE_KEY = "TkwkdsladkdlrhdnjfrmqdhodlfjgrpaksgdlwnjTdjdy";

    private final String sessionId;
    private final String hashcode;

    public MiracleTokenKey(HttpSession session) {
        this.sessionId = session.getId();
        this.hashcode = String.valueOf((session.getId()+PRIVATE_KEY).hashCode());
    }
}

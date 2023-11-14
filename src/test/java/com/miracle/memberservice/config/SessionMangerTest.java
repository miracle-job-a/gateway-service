package com.miracle.memberservice.config;

import com.miracle.memberservice.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class SessionMangerTest {

    SessionManger sessionManger = new SessionManger();
    @Test
    void sessionTest(){

        //세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        User user = new User();
        sessionManger.createSession(user, response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회
        Object result = sessionManger.getSession(request);
        assertThat(result).isEqualTo(user);

        //세션 만료
        sessionManger.expire(request);
        Object expired = sessionManger.getSession(request);
        assertThat(expired).isNull();
    }
}
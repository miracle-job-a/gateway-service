package com.miracle.memberservice.interceptor;

import com.miracle.memberservice.dto.response.TokenDto;
import com.miracle.memberservice.util.ServiceCall;
import com.miracle.memberservice.util.TokenMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TokenDto tokenDto = ServiceCall.tokenDto;
        tokenDto.setToken(TokenMethod.validateToken(tokenDto));
        return true;
    }
}

package com.miracle.memberservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class CompanyLoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/v1/company/faq/*", "/v1/company/post/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        log.debug("인증 체크 필터 시작");

        if (isLoginCheckPath(requestURI)) {
            log.trace("인증 체크 로직 실행 : " + requestURI);
            HttpSession session = httpRequest.getSession(false);
            if (session == null || session.getAttribute("bno") == null) {
                log.debug("미 인증 사용자 요청");
                httpResponse.sendRedirect("/v1/company/login-form");
                return; // 미인증 사용자는 다음으로 진행하지 않고 끝낸다.
            }
        }
        /* 다음 단계로 넘어간다. */
        chain.doFilter(request, response);
    }

    /*
    whitelist에 해당하는 url인 경우 인증 체크
    simpleMatch 	: 파라미터 문자열이 특정 패턴에 매칭되는지를 검사함.
    */
    private boolean isLoginCheckPath(String requestURI) {
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);

    }
}

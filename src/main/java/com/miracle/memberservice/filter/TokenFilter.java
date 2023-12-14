package com.miracle.memberservice.filter;

import com.miracle.memberservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter implements Filter {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private final List<String> excludeUriPatterns = new ArrayList<>();
    private final TokenService tokenService;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;

    public void addExcludeUriPattern(String... antPatterns) {
        excludeUriPatterns.addAll(List.of(antPatterns));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = httpServletRequest.getRequestURI();
        if (isExcluded(uri)) {
            chain.doFilter(request, response);
            return;
        }

        httpServletRequest = (HttpServletRequest) request;
        httpServletResponse = (HttpServletResponse) response;

        String bearerToken = getBearerToken();
        if (!tokenService.validateToken(bearerToken)) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isExcluded(String uri) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String pattern : excludeUriPatterns) {
            if (antPathMatcher.match(pattern, uri)) return true;
        }

        return false;
    }

    private String getBearerToken() throws IOException {
        String bearerToken = httpServletRequest.getHeader(HEADER_AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        httpServletResponse.sendError(HttpStatus.SC_UNAUTHORIZED);
        return null;
    }
}

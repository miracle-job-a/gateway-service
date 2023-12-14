package com.miracle.memberservice.filter;

import com.miracle.memberservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends HttpFilter {

    private final List<String> excludeUriPatterns = new ArrayList<>();
    private final TokenService tokenService;

    public void addExcludeUriPattern(String... antPatterns) {
        excludeUriPatterns.addAll(List.of(antPatterns));
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = request.getRequestURI();
        if (isExcluded(uri)) {
            chain.doFilter(request, response);
            return;
        }

        Optional<String> tokenOpt = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("token"))
                .map(Cookie::getValue)
                .findFirst();

        if (tokenOpt.isEmpty() || !tokenService.validateToken(tokenOpt.get())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
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
}

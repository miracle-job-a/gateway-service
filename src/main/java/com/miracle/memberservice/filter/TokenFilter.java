package com.miracle.memberservice.filter;

import com.miracle.memberservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

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

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<String> tokenOpt = Arrays.stream(cookies)
                    .filter(c -> c.getName().equals("token"))
                    .map(Cookie::getValue)
                    .findFirst();

            HttpSession session = request.getSession();
            if (tokenOpt.isEmpty() || !tokenService.validateToken(tokenOpt.get())) {
                session.getAttributeNames()
                        .asIterator()
                        .forEachRemaining(session::removeAttribute);

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String token = tokenOpt.get();
            Map<String, String> parsedToken = tokenService.parseToken(token);
            parsedToken.forEach((key, value) -> {
                if (key.equals("id")) {
                    session.setAttribute("id", Long.parseLong(value));
                } else if (key.equals("sub")) {
                    value = value.substring(value.indexOf(':') + 1);
                    session.setAttribute("email", value);
                } else {
                    session.setAttribute(key, value);
                }
            });
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

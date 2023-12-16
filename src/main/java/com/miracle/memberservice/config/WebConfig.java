package com.miracle.memberservice.config;

import com.miracle.memberservice.filter.AdminLoginCheckFilter;
import com.miracle.memberservice.filter.CompanyLoginCheckFilter;
import com.miracle.memberservice.filter.TokenFilter;
import com.miracle.memberservice.filter.UserLoginCheckFilter;
import com.miracle.memberservice.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import com.miracle.memberservice.service.TokenService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/v1/**");
    }

    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilter(TokenService tokenService) {
        FilterRegistrationBean<TokenFilter> filterRegistrationBean = new FilterRegistrationBean<>();

        TokenFilter tokenFilter = new TokenFilter(tokenService);
        tokenFilter.addExcludeUriPattern(
                "/v1",
                "/v1/{user,company,admin}/login-form",
                "/v1/{user,company}/join",
                "/v1/company/bno",
                "/v1/{user,company,admin}/login",
                "/v1/{user,company}/email/duplicate/{email}",
                "/v1/email/authentication/{authentication}",
                "/v1/search/{posts,total}/{strNum:\\d+}",
                "/v1/click/post/{postId:\\d+}/detail",
                "/v1/jwt/**"
        );

        filterRegistrationBean.setFilter(tokenFilter);
        filterRegistrationBean.setOrder(0);
        filterRegistrationBean.addUrlPatterns("/v1/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean userLogFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();

        filterRegistrationBean.setFilter(new UserLoginCheckFilter()); // LogFilter 등록
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/v1/user/resumes", "/v1/user/resume/*", "/v1/user/resume", "/v1/user/cover-letters/*", "/v1/user/cover-letter/*", "/v1/user/apply", "/v1/user/my-page/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean companyLogFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();

        filterRegistrationBean.setFilter(new CompanyLoginCheckFilter()); // LogFilter 등록
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/v1/company/faq/*", "/v1/company/post/*");
        return filterRegistrationBean;

    }

    @Bean
    public FilterRegistrationBean adminLogFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();

        filterRegistrationBean.setFilter(new AdminLoginCheckFilter()); // LogFilter 등록
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addUrlPatterns("/v1/admin/*");
        return filterRegistrationBean;

    }
}
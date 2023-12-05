package com.miracle.memberservice.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

@Component
public class WebConfig {

    @Bean
    public FilterRegistrationBean userLogFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();

        filterRegistrationBean.setFilter(new UserLoginCheckFilter()); // LogFilter 등록
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/your-url-pattern");
        return filterRegistrationBean;

    }

    @Bean
    public FilterRegistrationBean companyLogFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();

        filterRegistrationBean.setFilter(new CompanyLoginCheckFilter()); // LogFilter 등록
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/v1/company/faq/*");
        filterRegistrationBean.addUrlPatterns("/v1/company/post/*");
        return filterRegistrationBean;

    }

    @Bean
    public FilterRegistrationBean adminLogFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();

        filterRegistrationBean.setFilter(new CompanyLoginCheckFilter()); // LogFilter 등록
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addUrlPatterns("/v1/admin/*");
        return filterRegistrationBean;

    }
}
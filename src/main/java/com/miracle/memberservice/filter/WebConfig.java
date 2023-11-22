package com.miracle.memberservice.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

@Component
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();

        filterRegistrationBean.setFilter(new LoginCheckFilter()); // LogFilter 등록
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/v1/member");		// 모든 url 다 적용
        return filterRegistrationBean;

    }
}
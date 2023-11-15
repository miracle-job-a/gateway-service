package com.miracle.memberservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/v1")
public class GuestController {
    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/user/login-form")
    public String userLoginForm() {
        return "guest/user-login";
    }

    @GetMapping("/company/login-form")
    public String companyLoginForm() {
        return "guest/company-login";
    }

}

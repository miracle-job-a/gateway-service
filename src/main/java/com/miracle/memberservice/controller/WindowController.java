package com.miracle.memberservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("v1")
public class WindowController {
    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/company/join")
    public String companyJoin(){
        return "company-join";
    }

    @GetMapping("/login-form")
    public String loginForm(){
        return "login";
    }
}

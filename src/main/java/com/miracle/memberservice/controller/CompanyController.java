package com.miracle.memberservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/v1/company")
public class CompanyController {

    @GetMapping
    public String index(){
        return "company/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @GetMapping("/post/create")
    public String detail() {
        return "company/detail-page";
    }

    @GetMapping("/postlist")
    public String postList(){ return "company/post-list"; }

    @GetMapping("/post-form")
    public String postForm(){ return "company/post-form"; }

}

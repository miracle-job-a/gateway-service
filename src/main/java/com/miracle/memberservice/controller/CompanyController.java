package com.miracle.memberservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/company")
public class CompanyController {

    @GetMapping("/postlist")
    public String postList(){ return "company/post-list"; }

    @GetMapping("/post-form")
    public String postForm(){ return "company/post-form"; }
}

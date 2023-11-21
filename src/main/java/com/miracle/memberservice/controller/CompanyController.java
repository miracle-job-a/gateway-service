package com.miracle.memberservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/company")
public class CompanyController {

    @GetMapping("/post/create")
    public String detail(){
        return "company/detail-page";
    }
}

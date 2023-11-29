package com.miracle.memberservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/login")
    private String adminLogin(){ return "admin/login"; }
    @GetMapping("/users")
    private String userList(){ return "admin/userList"; }
    @GetMapping("/stacks")
    private String stackList(){ return "admin/stackList"; }
    @GetMapping("/jobs")
    private String jobList(){ return "admin/jobList"; }

}

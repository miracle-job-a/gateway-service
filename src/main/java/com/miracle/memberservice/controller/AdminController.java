package com.miracle.memberservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
    @GetMapping("/users")
    private String userList(){ return "admin/userList"; }

    @GetMapping("/stacks")
    private String stackList(){ return "admin/stackList"; }

    @GetMapping("/jobs")
    private String jobList(){ return "admin/jobList"; }

}

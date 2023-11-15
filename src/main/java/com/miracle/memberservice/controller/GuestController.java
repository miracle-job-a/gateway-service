package com.miracle.memberservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class GuestController {
    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/join")
    public String userJoin(){
        return "company-join";
    }
}

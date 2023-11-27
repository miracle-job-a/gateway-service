package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.response.UserBaseInfoResponseDto;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // [임시] 일반공고 상세페이지 이동
    @GetMapping("/normalPost")
    public String mzPostDatail(){ return "user/normal-post"; }

    // [임시] 이력서 생성 폼으로 이동
    @GetMapping("/resume/form")
    public String createResume(HttpSession session, Model model){
        PageMoveWithMessage pmwm = userService.formResume(session);
        model.addAttribute("info", (UserBaseInfoResponseDto)pmwm.getData());
        return pmwm.getPageName();
    }

    // [임시] 이력서 목록으로 이동
    @GetMapping("/resumes")
    public String resumeList(){ return "user/resume-list"; }

    // [임시] 자소서 목록으로 이동
    @GetMapping("/cover-letter")
    public String covetLetterList(){ return "user/cover-letter"; }

    @GetMapping("/coverLetterForm")
    public String createCoverLetter(){ return "user/coverLetterForm"; }
}

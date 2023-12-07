package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.response.CoverLetterInApplicationLetterResponseDto;
import com.miracle.memberservice.dto.response.JobResponseDto;
import com.miracle.memberservice.dto.response.ResumeInApplicationLetterResponseDto;
import com.miracle.memberservice.dto.response.StackResponseDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.MyPageService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/v1/user/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final UserService userService;
    private final AdminService adminService;

    // 임시 지원 자기소개서 이동
    @GetMapping("apply-list/submitted-coverLetter")
    public String submittedLetter(){ return "user/submitted-coverLetter"; }

    // TODO 마이페이지 목록 (추후 url 정리하기)
    @GetMapping("/apply-list/{startPage}")
    public String applyList(HttpSession session, Model model, @PathVariable(required = false) int startPage){
        PageMoveWithMessage pmwm = myPageService.applicationLetterList(session, startPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("letter", pmwm.getData());
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    @GetMapping("/apply-list/submitted-resume/{applicationLetterId}")
    public String applyResume(HttpSession session, Model model, @PathVariable(required = false) Long applicationLetterId){
        PageMoveWithMessage pmwm = myPageService.resumeInApplicationLetterDetail(session, applicationLetterId);

        ResumeInApplicationLetterResponseDto responseDto = (ResumeInApplicationLetterResponseDto) pmwm.getData();

        ArrayList<StackResponseDto> stacks = (ArrayList<StackResponseDto>) adminService.getStacks(session, responseDto.getUserStackIdSet());
        model.addAttribute("resume", pmwm.getData());
        model.addAttribute("stacks", stacks);
        return pmwm.getPageName();
    }

    @GetMapping("/apply-list/submitted-coverLetter/{applicationLetterId}")
    public String applyCoverLetter(HttpSession session, Model model, @PathVariable Long applicationLetterId){
        PageMoveWithMessage pmwm = myPageService.coverLetterInApplicationLetterDetail(session, applicationLetterId);

        model.addAttribute("letter", pmwm.getData());
        return pmwm.getPageName();
    }

}

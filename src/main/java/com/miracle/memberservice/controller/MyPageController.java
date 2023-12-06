package com.miracle.memberservice.controller;

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

    // 임시 마이페이지 이동
    @GetMapping("/apply-list")
    public String myPageList(){return "user/apply-list"; }
    // TODO 마이페이지 목록 (추후 url 정리하기)
    @GetMapping("/apply-list/{startPage}")
    public String applyList(HttpSession session, Model model, @PathVariable(required = false) int startPage){
        PageMoveWithMessage pmwm = myPageService.applicationLetterList(session, startPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("letter", pmwm.getData());
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    //TODO 임시 지원이력서 폼 이동 (추후 목록에서 이동 완성 시 수정하기)
    @GetMapping("/apply-list/submitted-resume")
    public String submittedResume(){return "user/submitted-resume"; }
    // 지원이력서 보기
    @GetMapping("/apply-list/resume")
    public String applyResume(HttpSession session, Model model){
        Long applicationLetterId = 1L;
        PageMoveWithMessage pmwm = myPageService.resumeInApplicationLetterDetail(session, applicationLetterId);

        ResumeInApplicationLetterResponseDto responseDto = (ResumeInApplicationLetterResponseDto) pmwm.getData();

        ArrayList<StackResponseDto> stacks = (ArrayList<StackResponseDto>) adminService.getStacks(session, responseDto.getUserStackIdSet());
        model.addAttribute("resume", pmwm.getData());
        model.addAttribute("stacks", stacks);
        return pmwm.getPageName();
    }
}

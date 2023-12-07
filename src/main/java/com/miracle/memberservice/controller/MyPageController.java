package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.InterviewRequestDto;
import com.miracle.memberservice.dto.request.QnaDto;
import com.miracle.memberservice.dto.request.QnaListDto;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/v1/user/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final UserService userService;
    private final AdminService adminService;


    // 마이페이지 목록
    @GetMapping("/apply-list/{startPage}")
    public String applyList(HttpSession session, Model model, @PathVariable(required = false) int startPage){
        PageMoveWithMessage pmwm = myPageService.applicationLetterList(session, startPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("letter", pmwm.getData());
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    // 지원 이력서
    @GetMapping("/apply-list/submitted-resume/{applicationLetterId}")
    public String applyResume(HttpSession session, Model model, @PathVariable(required = false) Long applicationLetterId){
        PageMoveWithMessage pmwm = myPageService.resumeInApplicationLetterDetail(session, applicationLetterId);

        ResumeInApplicationLetterResponseDto responseDto = (ResumeInApplicationLetterResponseDto) pmwm.getData();

        ArrayList<StackResponseDto> stacks = (ArrayList<StackResponseDto>) adminService.getStacks(session, responseDto.getUserStackIdSet());
        model.addAttribute("resume", pmwm.getData());
        model.addAttribute("stacks", stacks);
        return pmwm.getPageName();
    }

    // 지원 자소서
    @GetMapping("/apply-list/submitted-coverLetter/{applicationLetterId}")
    public String applyCoverLetter(HttpSession session, Model model, @PathVariable Long applicationLetterId){
        PageMoveWithMessage pmwm = myPageService.coverLetterInApplicationLetterDetail(session, applicationLetterId);

        model.addAttribute("letter", pmwm.getData());
        return pmwm.getPageName();
    }

    // 지원 면접생성 폼 이동
    @GetMapping("/interview/form/{applicationLetterId}")
    public String interviewForm(@PathVariable Long applicationLetterId, Model model){
        model.addAttribute("applicationLetterId", applicationLetterId);
        return "user/interview-form";
    }

    // 면접 저장
    @PostMapping("/interview")
    public String createInterview(@ModelAttribute QnaListDto qnaListDto, Long applicationLetterId, HttpSession session){
        List<QnaDto> qnaDtoList = new ArrayList<>();
        for (int i = 0; i < qnaListDto.getAnswer().size(); i++){
            String question = qnaListDto.getQuestion().get(i);
            String answer = qnaListDto.getAnswer().get(i);
            qnaDtoList.add(new QnaDto(question, answer));
        }

        PageMoveWithMessage pmwm = myPageService.createInterview(session, new InterviewRequestDto(applicationLetterId, qnaDtoList));
        return pmwm.getPageName();
    }

    // 면접 조회
    @GetMapping("/interview/{interviewId}")
    public String interviewDetail(@PathVariable Long interviewId, HttpSession session, Model model) {
        PageMoveWithMessage pmwm = myPageService.interviewDetail(session, interviewId);
        model.addAttribute("interview", pmwm.getData());
        model.addAttribute("interviewId", interviewId);
        return pmwm.getPageName();
    }

    // 면접 삭제
    @GetMapping("/interview/delete/{interviewId}")
    public String deleteInterview(@PathVariable Long interviewId, HttpSession session){
        PageMoveWithMessage pmwm = myPageService.deleteInterview(session, interviewId);
        return pmwm.getPageName();
    }
}

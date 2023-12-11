package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.*;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.MyPageService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/v1/user/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final AdminService adminService;


    // 마이페이지 목록
    @GetMapping("/apply-list/{startPage}")
    public String applyList(HttpSession session, Model model, @PathVariable(required = false) int startPage, @RequestParam(required = false, defaultValue = "SUBMIT_DATE_DESC") String sort) {
        PageMoveWithMessage pmwm = myPageService.applicationLetterList(session, startPage, sort);

        List<List<ApplicationLetterListResponseDto>> data = (List<List<ApplicationLetterListResponseDto>>) pmwm.getData();

        Set<Long> postIdSet = new HashSet<>();
        data.iterator().forEachRemaining((List<ApplicationLetterListResponseDto> list) -> list.iterator().forEachRemaining((ApplicationLetterListResponseDto dto) -> postIdSet.add(dto.getPostId())));
        List<CompanyNameResponseDto> info = myPageService.getCompanyInfo(session, postIdSet);

        model.addAttribute("info", info);
        model.addAttribute("startPage", startPage);
        model.addAttribute("letter", pmwm.getData());
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        model.addAttribute("sort", sort);
        return pmwm.getPageName();
    }

    // 지원서 취소(삭제)
    @GetMapping("/apply-list/delete/{applicationLetterId}")
    public String deleteApplicationLetter(@PathVariable Long applicationLetterId, HttpSession session){
        PageMoveWithMessage pmwm = myPageService.deleteApplicationLetter(session, applicationLetterId);
        return pmwm.getPageName();
    }

    // 지원서 상태 변경
    @GetMapping("/apply-list/update/{applicationLetterId}/{applicationStatus}")
    public String updateApplicationLetter(@PathVariable Long applicationLetterId, @PathVariable String applicationStatus,
                                          HttpSession session){
        PageMoveWithMessage pmwm = myPageService.updateApplicationLetter(session, applicationLetterId, applicationStatus);
        return pmwm.getPageName();
    }


    // 지원 이력서
    @GetMapping("/apply-list/submitted-resume/{applicationLetterId}")
    public String applyResume(HttpSession session, Model model, @PathVariable(required = false) Long applicationLetterId, @RequestParam(required = false) Long userId) {
        PageMoveWithMessage pmwm = myPageService.resumeInApplicationLetterDetail(session, applicationLetterId, userId);

        ResumeInApplicationLetterResponseDto responseDto = (ResumeInApplicationLetterResponseDto) pmwm.getData();

        ArrayList<StackResponseDto> stacks = (ArrayList<StackResponseDto>) adminService.getStacks(session, responseDto.getUserStackIdSet());
        model.addAttribute("resume", pmwm.getData());
        model.addAttribute("stacks", stacks);
        return pmwm.getPageName();
    }

    // 지원 자소서
    @GetMapping("/apply-list/submitted-coverLetter/{applicationLetterId}")
    public String applyCoverLetter(HttpSession session, Model model, @PathVariable Long applicationLetterId, @RequestParam(required = false) Long userId) {
        PageMoveWithMessage pmwm = myPageService.coverLetterInApplicationLetterDetail(session, applicationLetterId, userId);

        model.addAttribute("letter", pmwm.getData());
        return pmwm.getPageName();
    }

    // 지원 면접생성 폼 이동
    @GetMapping("/interview/form/{applicationLetterId}")
    public String interviewForm(@PathVariable Long applicationLetterId, @RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("applicationLetterId", applicationLetterId);
        model.addAttribute("errorMessage", errorMessage);
        return "user/interview-form";
    }

    // 면접 저장
    @PostMapping("/interview")
    public String createInterview(RedirectAttributes redirectAttributes, @ModelAttribute QnaListDto qnaListDto, Long applicationLetterId, HttpSession session) {
        List<QnaDto> qnaDtoList = new ArrayList<>();
        for (int i = 0; i < qnaListDto.getAnswer().size(); i++) {
            String question = qnaListDto.getQuestion().get(i);
            String answer = qnaListDto.getAnswer().get(i);
            qnaDtoList.add(new QnaDto(question, answer));
        }

        PageMoveWithMessage pmwm = myPageService.createInterview(session, new InterviewRequestDto(applicationLetterId, qnaDtoList), applicationLetterId);
        redirectAttributes.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    // 면접 수정
    @PostMapping("/interview/update")
    public String updateInterview(@ModelAttribute QnaListDto qnaListDto,
                                  Long applicationLetterId, Long interviewId, HttpSession session) {
        List<QnaDto> qnaDtoList = new ArrayList<>();
        for (int i = 0; i < qnaListDto.getAnswer().size(); i++) {
            String question = qnaListDto.getQuestion().get(i);
            String answer = qnaListDto.getAnswer().get(i);
            qnaDtoList.add(new QnaDto(question, answer));
        }

        PageMoveWithMessage pmwm = myPageService.updateInterview(session, new InterviewRequestDto(applicationLetterId, qnaDtoList), interviewId, applicationLetterId);
        return pmwm.getPageName();
    }

    // 면접 조회
    @GetMapping("/interview/{applicationLetterId}/{interviewId}")
    public String interviewDetail(@PathVariable Long interviewId,
                                  @PathVariable Long applicationLetterId, HttpSession session, Model model) {
        PageMoveWithMessage pmwm = myPageService.interviewDetail(session, interviewId);
        model.addAttribute("interview", pmwm.getData());
        model.addAttribute("interviewId", interviewId);
        model.addAttribute("applicationLetterId", applicationLetterId);
        return pmwm.getPageName();
    }

    // 면접 삭제
    @GetMapping("/interview/delete/{interviewId}")
    public String deleteInterview(@PathVariable Long interviewId, HttpSession session) {
        PageMoveWithMessage pmwm = myPageService.deleteInterview(session, interviewId);
        return pmwm.getPageName();
    }

    // 개인정보 조회 (임시)
    @GetMapping("/my-info")
    public String getPersonal(HttpSession session, Model model){
        PageMoveWithMessage pmwm = myPageService.userInfo(session);
        UserInfoResponseDto data = (UserInfoResponseDto) pmwm.getData();
        ArrayList<StackResponseDto> stacks = (ArrayList<StackResponseDto>) adminService.getStacks(session, data.getStackIdSet());
        ArrayList<StackResponseDto> allStack = (ArrayList<StackResponseDto>) adminService.getAllStacks(session);
        model.addAttribute("info", pmwm.getData());
        model.addAttribute("stacks", stacks);
        model.addAttribute("allStack", allStack);
        return pmwm.getPageName();
    }

    // 개인정보 수정 인증접근
    @GetMapping("/my-info/move")
    public String userModify() { return "user/validation"; }

    // 개인정보 수정 인증
    @PostMapping("/my-info/validation")
    public String checkUserInfo(HttpSession session, @ModelAttribute LoginDto loginDto, String password){
        PageMoveWithMessage pmwm = myPageService.validationUser(session, loginDto);
        if ("로그인에 성공했습니다.".equals(pmwm.getErrorMessage())) {
            session.setAttribute("password", password);
            return "redirect:/v1/user/my-page/my-info/modify";
        } else {
            return "redirect:/v1/user/my-page/my-info/move";
        }
    }

    // 개인정보 수정 폼 이동
    @GetMapping("/my-info/modify")
    public String modifyUserInfo(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = myPageService.modifyUserInfo(session);
        UserInfoResponseDto data = (UserInfoResponseDto) pmwm.getData();
        ArrayList<StackResponseDto> stacks = (ArrayList<StackResponseDto>) adminService.getStacks(session, data.getStackIdSet());
        ArrayList<StackResponseDto> allStack = (ArrayList<StackResponseDto>) adminService.getAllStacks(session);
        model.addAttribute("info", pmwm.getData());
        model.addAttribute("stacks", stacks);
        model.addAttribute("allStack", allStack);
        return pmwm.getPageName();
    }

    @PostMapping("/my-info/update")
    public String updateUserInfo(HttpSession session, @ModelAttribute UserUpdateInfoRequestDto requestDto) {
        if (requestDto.getPassword() == null || requestDto.getPassword().isEmpty()){
            String password = (String) session.getAttribute("password");
            requestDto.setPassword(password);
        }
        PageMoveWithMessage pmwm = myPageService.updateUserInfo(session, requestDto);
        return pmwm.getPageName();
    }
}

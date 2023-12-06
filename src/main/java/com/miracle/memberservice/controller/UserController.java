package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.JobResponseDto;
import com.miracle.memberservice.dto.response.ResumeResponseDto;
import com.miracle.memberservice.dto.response.StackResponseDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AdminService adminService;

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @GetMapping("/resume/form")
    public String createResume(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = userService.formResume(session);
        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        model.addAttribute("info", pmwm.getData());
        model.addAttribute("jobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("stacks", allJobsAndStacks.get("stacks"));
        return pmwm.getPageName();
    }

    @PostMapping("/resume")
    public String addResume(RedirectAttributes redirectAttributes, HttpSession session, ResumeRequestDto resumeRequestDto) {
        PageMoveWithMessage pmwm = userService.addResume(session, resumeRequestDto);
        redirectAttributes.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    @GetMapping("/resumes")
    public String resumeList(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = userService.resumeList(session);

        List<JobResponseDto> jobs = adminService.getAllJobs(session);
        model.addAttribute("resumeList", pmwm.getData());
        model.addAttribute("jobs", jobs);
        model.addAttribute("errorMessage", pmwm.getErrorMessage());

        return pmwm.getPageName();
    }

    @GetMapping("/resume/detail/{resumeId}")
    public String resumeDetail(HttpSession session, Model model, @PathVariable Long resumeId) {
        PageMoveWithMessage pmwm = userService.getResumeDetail(session, resumeId);
        PageMoveWithMessage pmwm2 = userService.formResume(session);

        ResumeResponseDto data = (ResumeResponseDto) pmwm.getData();

        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        ArrayList<JobResponseDto> jobs = (ArrayList<JobResponseDto>) adminService.getJobs(session, data.getJobIdSet());
        ArrayList<StackResponseDto> stacks = (ArrayList<StackResponseDto>) adminService.getStacks(session, data.getStackIdSet());

        model.addAttribute("resume", data);
        model.addAttribute("info", pmwm2.getData());
        model.addAttribute("jobs", jobs);
        model.addAttribute("stacks", stacks);
        model.addAttribute("totalJobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("totalStacks", allJobsAndStacks.get("stacks"));

        return pmwm.getPageName();
    }

    @GetMapping("/resume/delete/{resumeId}")
    public String deleteResume(HttpSession session, @PathVariable Long resumeId) {
        PageMoveWithMessage pmwm = userService.deleteResume(session, resumeId);
        return pmwm.getPageName();
    }

    @PostMapping("/resume/update/{resumeId}")
    public String updateResume(HttpSession session,
                               @PathVariable Long resumeId,
                               @ModelAttribute ResumeRequestDto requestDto,
                               RedirectAttributes redirectAttributes) {
        PageMoveWithMessage pmwm = userService.updateResume(session, requestDto, resumeId);
        redirectAttributes.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    // [임시] 자소서 목록으로 이동
    @GetMapping("/cover-letters/{strNum}")
    public String coverLetterList(HttpSession session, Model model, @PathVariable(required = false) int strNum) {
        PageMoveWithMessage pmwm = userService.coverLetterList(session, strNum);
        model.addAttribute("letterList", pmwm.getData());
        model.addAttribute("strNum", strNum);
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    @GetMapping("/cover-letter/form")
    public String coverLetterForm() {
        return "user/coverLetter-form";
    }

    @PostMapping("/cover-letter/create")
    public String createCoverLetter(String title, @ModelAttribute QnaListDto qnaListDto, HttpSession session) {
        List<QnaDto> qnaDtoList = new ArrayList<>();
        for (int i = 0; i < qnaListDto.getAnswer().size(); i++) {
            String question = qnaListDto.getQuestion().get(i);
            String answer = qnaListDto.getAnswer().get(i);
            qnaDtoList.add(new QnaDto(question, answer));
        }

        PageMoveWithMessage pmwm = userService.createCoverLetter(session, new CoverLetterPostRequestDto(title, qnaDtoList));
        return pmwm.getPageName();
    }

    @GetMapping("/cover-letter/delete/{coverLetterId}")
    public String deleteCoverLetter(HttpSession session, @PathVariable Long coverLetterId) {
        PageMoveWithMessage pmwm = userService.deleteCoverLetter(session, coverLetterId);
        return pmwm.getPageName();
    }

    @GetMapping("/cover-letter/detail/{id}")
    public String coverLetterDetail(HttpSession session, @PathVariable Long id, Model model) {
        PageMoveWithMessage pmwm = userService.coverLetterDetail(session, id);
        model.addAttribute("coverLetter", pmwm.getData());
        return pmwm.getPageName();
    }

    @PostMapping("/cover-letter/update")
    public String updateCoverLetter(String title, Long id, @ModelAttribute QnaListDto qnaListDto, HttpSession session) {
        System.out.println(id);
        List<QnaDto> qnaDtoList = new ArrayList<>();
        for (int i = 0; i < qnaListDto.getAnswer().size(); i++) {
            String question = qnaListDto.getQuestion().get(i);
            String answer = qnaListDto.getAnswer().get(i);
            qnaDtoList.add(new QnaDto(question, answer));
        }
        PageMoveWithMessage pmwm = userService.updateCoverLetter(session, new CoverLetterPostRequestDto(title, qnaDtoList), id);
        return pmwm.getPageName();
    }

    @PostMapping("/apply/{companyId}")
    public String apply(RedirectAttributes redirectAttributes, HttpSession session, @ModelAttribute ApplicationLetterPostRequestDto dto, @PathVariable Long companyId) {
        PageMoveWithMessage pmwm = userService.apply(session, dto, companyId);
        redirectAttributes.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }
}

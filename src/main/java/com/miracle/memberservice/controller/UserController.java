package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.*;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AdminService adminService;
    private final CompanyService companyService;

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/v1";
    }

    @GetMapping("/resume/form")
    public String createResume(HttpSession session, Model model, @RequestParam(required = false) String postId, @RequestParam(required = false) String companyId, @RequestParam(required = false) String postType) {
        PageMoveWithMessage pmwm = userService.formResume(session);
        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);

        UserBaseInfoResponseDto data = (UserBaseInfoResponseDto) pmwm.getData();
        List<StackResponseDto> stacks = adminService.getStacks(session, data.getStackIdSet());

        model.addAttribute("info", pmwm.getData());
        model.addAttribute("jobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("stacks", allJobsAndStacks.get("stacks"));
        model.addAttribute("stackIdSet", stacks);
        model.addAttribute("postId", postId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("postType", postType);
        return pmwm.getPageName();
    }

    @PostMapping("/resume")
    public String addResume(RedirectAttributes redirectAttributes, HttpSession session, ResumeRequestDto resumeRequestDto, @RequestParam MultipartFile file) throws IOException {
        PageMoveWithMessage pmwm = userService.addResume(session, resumeRequestDto, file);
        if (Objects.nonNull(resumeRequestDto.getPostId())) {
            redirectAttributes.addAttribute("companyId", resumeRequestDto.getCompanyId());
            redirectAttributes.addAttribute("postType", resumeRequestDto.getPostType());
        }
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
    public String deleteResume(HttpSession session, @PathVariable Long resumeId, @RequestParam(required = false) String photo) {
        PageMoveWithMessage pmwm = userService.deleteResume(session, resumeId, photo);
        return pmwm.getPageName();
    }

    @PostMapping("/resume/update/{resumeId}")
    public String updateResume(HttpSession session,
                               @PathVariable Long resumeId,
                               @ModelAttribute ResumeRequestDto requestDto,
                               RedirectAttributes redirectAttributes,
                               @RequestParam MultipartFile file) throws IOException {
        PageMoveWithMessage pmwm = userService.updateResume(session, requestDto, resumeId, file);
        redirectAttributes.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    // 자소서 목록으로 이동
    @GetMapping("/cover-letters/{strNum}")
    public String coverLetterList(HttpSession session, Model model, @PathVariable(required = false) int strNum, @RequestParam(required = false, defaultValue = "MODIFIED_AT_DESC") String sort, @RequestParam(required = false, defaultValue = "") String word) {
        PageMoveWithMessage pmwm;
        if (word.isEmpty()) {
            pmwm = userService.coverLetterList(session, strNum, sort);
        } else {
            pmwm = userService.coverLetterListSearch(session, strNum, word);
        }
        model.addAttribute("letterList", pmwm.getData());
        model.addAttribute("strNum", strNum);
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        model.addAttribute("sort", sort);
        return pmwm.getPageName();
    }

    @GetMapping("/cover-letter/form")
    public String coverLetterForm(HttpSession session, @RequestParam(required = false) Long postId, @RequestParam(required = false) Long companyId, @RequestParam(required = false) String postType, @RequestParam(required = false) String errorMessage, Model model) {
        if(Objects.nonNull(postId)){
            PageMoveWithMessage postDetail = companyService.getPostDetail(session, postId, postType, companyId);
            model.addAttribute("detail", postDetail.getData());
        }
        model.addAttribute("postId", postId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("postType", postType);
        model.addAttribute("errorMessage", errorMessage);
        return "user/coverLetter-form";
    }

    @PostMapping("/cover-letter/create")
    public String createCoverLetter(RedirectAttributes redirectAttributes, String title, @ModelAttribute QnaListDto qnaListDto, HttpSession session) {
        List<QnaDto> qnaDtoList = new ArrayList<>();
        for (int i = 0; i < qnaListDto.getAnswer().size(); i++) {
            String question = qnaListDto.getQuestion().get(i);
            String answer = qnaListDto.getAnswer().get(i);
            qnaDtoList.add(new QnaDto(question, answer));
        }
        if (Objects.nonNull(qnaListDto.getPostId())) {
            redirectAttributes.addAttribute("companyId", qnaListDto.getCompanyId());
            redirectAttributes.addAttribute("postId", qnaListDto.getPostId());
            redirectAttributes.addAttribute("postType", qnaListDto.getPostType());
        }
        PageMoveWithMessage pmwm = userService.createCoverLetter(session, new CoverLetterPostRequestDto(title, qnaDtoList), qnaListDto);
        redirectAttributes.addAttribute("errorMessage", pmwm.getErrorMessage());
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
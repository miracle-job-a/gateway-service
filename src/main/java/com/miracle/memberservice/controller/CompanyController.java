package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.CompanyFaqRequestDto;
import com.miracle.memberservice.dto.request.MzPostDto;
import com.miracle.memberservice.dto.request.PostRequestDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final AdminService adminService;

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @GetMapping("/post/list")
    public String postList(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = companyService.postList(session);
        model.addAttribute("postList", pmwm.getData());
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    // 공고 생성 폼 이동
    @GetMapping("/post/form")
    public String postFormPage(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = companyService.formPost(session);
        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        model.addAttribute("info", pmwm.getData());
        model.addAttribute("jobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("stacks", allJobsAndStacks.get("stacks"));
        return pmwm.getPageName();
    }

    @GetMapping("/post/mz/form")
    public String postForm() {
        return "company/mz-post";
    }

    @PostMapping("/post/create")
    public String createPost(@ModelAttribute PostRequestDto postRequestDto, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.createPost(session, postRequestDto);

        return pmwm.getPageName();
    }

    @PostMapping("/post/mz/create")
    public String createMZ(@ModelAttribute MzPostDto mzPostDto, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.createMZ(mzPostDto, session);
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    // 기업 자주하는 질문페이지 이동
    @GetMapping("/faq")
    public String faqList(@RequestParam(value = "errorMessage", required = false) String errorMessage, HttpSession session, Model model) {
        PageMoveWithMessage pmwm = companyService.faqList(session);

        if (Objects.nonNull(pmwm.getErrorMessage())) {
            model.addAttribute("errorMessage", pmwm.getErrorMessage());
        } else {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("faqList", pmwm.getData());
        return pmwm.getPageName();
    }

    @GetMapping("/faq/add")
    public String addFaq(RedirectAttributes redirectAttributes, HttpSession session, CompanyFaqRequestDto companyFaqRequestDto) {
        PageMoveWithMessage pmwm = companyService.addFaq(session, companyFaqRequestDto);
        redirectAttributes.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    @GetMapping("/faq/delete/{faqId}")
    public String deleteFaq(RedirectAttributes redirectAttributes, @PathVariable String faqId, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.deleteFaq(session, faqId);
        redirectAttributes.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

}

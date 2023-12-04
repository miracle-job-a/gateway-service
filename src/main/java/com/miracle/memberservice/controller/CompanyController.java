package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.JobResponseDto;
import com.miracle.memberservice.dto.response.PostResponseDto;
import com.miracle.memberservice.dto.response.StackResponseDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @GetMapping("/post/list/{strNum}")
    public String postList(HttpSession session, Model model, @PathVariable int strNum) {
        PageMoveWithMessage pmwm = companyService.postList(session, strNum, strNum+4);
        model.addAttribute("strNum", strNum);
        model.addAttribute("postPage", pmwm.getData());
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    // 공고 생성 폼 이동
    @GetMapping("/post/form")
    public String postFormPage(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = companyService.formPost(session, null);
        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        model.addAttribute("info", pmwm.getData());
        model.addAttribute("jobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("stacks", allJobsAndStacks.get("stacks"));
        return pmwm.getPageName();
    }

    @GetMapping("/post/detail")
    public String postDetail(HttpSession session, Model model, @RequestParam(name = "id") Long postId, @RequestParam String postType) {
        PageMoveWithMessage pmwm = companyService.getPostDetail(session, postId, postType);
        PageMoveWithMessage pmwm2 = companyService.formPost(session, null);

        PostResponseDto data = (PostResponseDto) pmwm.getData();

        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        List<JobResponseDto> jobs = adminService.getJobs(session, data.getJobIdSet());
        List<StackResponseDto> stacks = adminService.getStacks(session, data.getStackIdSet());

        model.addAttribute("postType", postType);
        model.addAttribute("postId", postId);
        model.addAttribute("info", pmwm2.getData());
        model.addAttribute("detail", data);
        model.addAttribute("jobs", jobs);
        model.addAttribute("stacks", stacks);
        model.addAttribute("totalJobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("totalStacks", allJobsAndStacks.get("stacks"));
        return pmwm.getPageName();
    }

    @PostMapping("/post/update")
    public String updatePost(HttpSession session, @ModelAttribute PostRequestDto postEditRequestDto, @ModelAttribute(binding = false) IdEditDto idList, @ModelAttribute(binding = false) QuestionEditDto questionList) {
        PageMoveWithMessage pmwm = companyService.updatePost(session, postEditRequestDto, idList, questionList);

        return pmwm.getPageName();
    }

    @GetMapping("/post/delete/{postId}")
    public String deletePost(HttpSession session, @PathVariable Long postId) {
        PageMoveWithMessage pmwm = companyService.deletePost(session, postId);
        return pmwm.getPageName();
    }

    @GetMapping("/post/close/{postId}/{postType}")
    public String closePost(HttpSession session, @PathVariable Long postId, @PathVariable String postType) {
        PageMoveWithMessage pmwm = companyService.closePost(session, postId, postType);
        return pmwm.getPageName();
    }

    @GetMapping("/post/{postType}/form")
    public String postForm(HttpSession session, Model model, @PathVariable String postType) {
        PageMoveWithMessage pmwm = companyService.formPost(session, postType);
        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        model.addAttribute("info", pmwm.getData());
        model.addAttribute("jobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("stacks", allJobsAndStacks.get("stacks"));
        return pmwm.getPageName();
    }

    @PostMapping("/post/create")
    public String createPost(@ModelAttribute PostCreateRequestDto postRequestDto, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.createPost(session, postRequestDto);

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

package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.JobResponseDto;
import com.miracle.memberservice.dto.response.PostResponseDto;
import com.miracle.memberservice.dto.response.StackResponseDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final AdminService adminService;
    private final UserService userService;
    @Value("${miracle.passwordChecker}")
    private String passwordChecker;

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/v1";
    }

    @GetMapping("/post/list/{strNum}")
    public String postList(HttpSession session, Model model, @PathVariable int strNum, @RequestParam(required = false, defaultValue = "") String sort, @RequestParam(required = false) String errorMessage) {
        PageMoveWithMessage pmwm = companyService.postList(session, strNum, strNum + 4, sort);
        model.addAttribute("strNum", strNum);
        model.addAttribute("postPage", pmwm.getData());
        model.addAttribute("errorMessage", errorMessage);
        if (Strings.isBlank(sort)) sort = "latest";
        model.addAttribute("sort", sort);
        return pmwm.getPageName();
    }

    // 공고 생성 폼 이동
    @GetMapping("/post/form")
    public String postFormPage(RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        if (!companyService.statusCompany(session)) {
            redirectAttributes.addAttribute("errorMessage", "현재 가입 승인 전입니다. \n관리자 승인 이전에 공고 생성은 불가합니다.");
            return "redirect:/v1/company/post/list/1";
        }
        PageMoveWithMessage pmwm = companyService.formPost(session, null, null);
        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        model.addAttribute("info", pmwm.getData());
        model.addAttribute("jobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("stacks", allJobsAndStacks.get("stacks"));
        return pmwm.getPageName();
    }

    @GetMapping("/post/detail")
    public String postDetail(HttpSession session, Model model, @RequestParam(name = "id") Long postId, @RequestParam String postType) {
        PageMoveWithMessage pmwm = companyService.getPostDetail(session, postId, postType, null);
        PageMoveWithMessage pmwm2 = companyService.formPost(session, null, null);

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
    public String postForm(RedirectAttributes redirectAttributes, HttpSession session, Model model, @PathVariable String postType) {
        if (!companyService.statusCompany(session)) {
            redirectAttributes.addAttribute("errorMessage", "현재 가입 승인 전입니다. \n관리자 승인 이전에 공고 생성은 불가합니다.");
            return "redirect:/v1/company/post/list/1";
        }
        PageMoveWithMessage pmwm = companyService.formPost(session, postType, null);
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

    @GetMapping("/post/applicant/{startPage}")
    public String applicantList(@PathVariable int startPage, @RequestParam Long postId, @RequestParam(required = false, defaultValue = "SUBMIT_DATE_DESC") String sort, HttpSession session, Model model) {
        PageMoveWithMessage pmwm = userService.applicantList(session, postId, sort, startPage);
        model.addAttribute("applicantList", pmwm.getData());
        model.addAttribute("strNum", startPage);
        model.addAttribute("sort", sort);
        model.addAttribute("postId", postId);
        return pmwm.getPageName();
    }

    @GetMapping("/today/signUp/list")
    public String getCompanyList(Model model, @RequestParam int strNum, @RequestParam boolean today, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.getCompanyListToday(session, strNum, strNum + 4, today);
        model.addAttribute("strNum", strNum);
        model.addAttribute("today", today);
        model.addAttribute("companyListPage", pmwm.getData());
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    //기업정보 상세보기 (임시)
    @GetMapping("/info")
    public String getCompanyInfo(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = companyService.getCompanyInfo(session);
        model.addAttribute("info", pmwm.getData());
        return pmwm.getPageName();
    }

    // 수정페이지 전 이동
    @GetMapping("/info/move")
    public String companyMdfy(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "company/validation";
    }

    // 수정페이지 요청
    @PostMapping("/info/validation")
    public String checkCompanyInfo(HttpSession session, @ModelAttribute CompanyLoginRequestDto requestDto, RedirectAttributes redirectAttributes) {
        boolean checked = companyService.checkCompanyInfo(session, requestDto);
        if (checked) {
            return "redirect:/v1/company/info/modify";
        } else {
            redirectAttributes.addAttribute("errorMessage", "로그인 정보가 일치하지 않습니다. ");
            return "redirect:/v1/company/info/move";
        }
    }

    @GetMapping("/info/modify")
    public String modifyCompanyInfo(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = companyService.modifyCompanyInfo(session);
        model.addAttribute("info", pmwm.getData());
        return pmwm.getPageName();
    }


    @PostMapping("/info/update")
    public String updateCompanyInfo(HttpSession session, @ModelAttribute CompanyInfoRequestDto requestDto, @RequestParam MultipartFile photo) throws IOException {
        if (requestDto.getPwd() == null || requestDto.getPwd().isEmpty()) {
            requestDto.setPwd(passwordChecker);
        }
        PageMoveWithMessage pmwm = companyService.updateCompanyInfo(session, requestDto, photo);
        return pmwm.getPageName();
    }

    @GetMapping("/signout")
    public String signoutCompany(HttpSession session) {
        PageMoveWithMessage pmwm = companyService.signoutCompany(session);
        return pmwm.getPageName();
    }
}

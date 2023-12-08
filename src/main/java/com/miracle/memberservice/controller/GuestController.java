package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.ApplicationLetterResponseDto;
import com.miracle.memberservice.dto.response.JobResponseDto;
import com.miracle.memberservice.dto.response.PostResponseDto;
import com.miracle.memberservice.dto.response.StackResponseDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.service.EmailService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
@RequestMapping("/v1")
@RequiredArgsConstructor
public class GuestController {

    private final UserService userService;
    private final CompanyService companyService;
    private final EmailService emailService;
    private final AdminService adminService;

    @GetMapping
    public String index(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = companyService.mainPage(session);
        if (Objects.nonNull(session.getAttribute("bno"))) {
            Long id = (Long) session.getAttribute("id");
            model.addAttribute("count", companyService.mainPageCompany(session, id));
        }
        model.addAttribute("map", pmwm.getData());
        return "index";
    }

    //회원가입 로그인 폼
    @GetMapping("/user/login-form")
    public String userLoginForm(@RequestParam(required = false) Long postId, @RequestParam(required = false) Long companyId, @RequestParam(required = false) String postType, Model model) {
        model.addAttribute("postId", postId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("postType", postType);
        return "guest/user-login";
    }

    @GetMapping("/company/login-form")
    public String companyLoginForm() {
        return "guest/company-login";
    }

    @GetMapping("/admin/login-form")
    public String adminLoginForm() {
        return "guest/admin-login";
    }

    //회원가입 폼 이동
    @GetMapping("/user/join")
    public String userJoinPage() {
        return "guest/user-join";
    }

    @GetMapping("/company/join")
    public String companyJoinPage() {
        return "guest/company-join";
    }

    //사업자 번호 조회 API
    @PostMapping(value = "/company/bno")
    public ResponseEntity<String> bnoCertify(@ModelAttribute CompanyCheckBnoRequestDto bno, HttpSession session) {
        return companyService.bnoCertify(bno, session);
    }

    // 회원가입 API
    @PostMapping("/user/join")
    public String userJoin(@ModelAttribute UserJoinDto userJoinDto, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = userService.join(userJoinDto, session);
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    @PostMapping("/company/join")
    public String companyJoin(@ModelAttribute CompanyJoinDto companyJoinDto, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.join(companyJoinDto, session);
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    //로그인 API
    @PostMapping("/company/login")
    public String companyLogin(@ModelAttribute LoginDto loginDto, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.login(loginDto, session);
        String pageName = pmwm.getPageName();

        if (pmwm.getId() != null) {
            session.setAttribute("id", pmwm.getId());
            session.setAttribute("email", pmwm.getEmail());
            session.setAttribute("bno", pmwm.getNameOrBno());
        }

        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pageName;
    }

    @PostMapping("/user/login")
    public String userLogin(RedirectAttributes redirectAttributes, @ModelAttribute LoginDto loginDto, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = userService.login(loginDto, session);
        if (pmwm.getId() != null) {
            session.setAttribute("id", pmwm.getId());
            session.setAttribute("email", pmwm.getEmail());
            session.setAttribute("name", pmwm.getNameOrBno());
        }
        if (Objects.nonNull(loginDto.getPostId())) {
            redirectAttributes.addAttribute("companyId", loginDto.getCompanyId());
            redirectAttributes.addAttribute("postType", loginDto.getPostType());
        }
        String errorMessage = pmwm.getErrorMessage();
        model.addAttribute("errorMessage", errorMessage);
        return pmwm.getPageName();
    }

    @PostMapping("/admin/login")
    public String adminLogin(@ModelAttribute LoginDto loginDto, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = adminService.login(loginDto, session);
        String pageName = pmwm.getPageName();

        if (pmwm.getId() != null) {
            session.setAttribute("id", pmwm.getId());
            session.setAttribute("email", pmwm.getEmail());
        }

        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pageName;
    }

    @GetMapping("/user/email/duplicate/{email}")
    public ResponseEntity<String> userEmailDuplicationCheck(@PathVariable String email, HttpSession session) throws MessagingException {
        ResponseEntity<String> response = userService.duplicateEmail(session, email);

        if (!response.getStatusCode().isError()) {
            String key = emailService.sendMail(email);
            session.setAttribute("key", key);
            session.setMaxInactiveInterval((int) TimeUnit.MINUTES.toSeconds(10));
        }
        return response;
    }

    @GetMapping("/company/email/duplicate/{email}")
    public ResponseEntity<String> companyEmailDuplicationCheck(@PathVariable String email, HttpSession session) throws MessagingException {
        ResponseEntity<String> response = companyService.duplicateEmail(session, email);

        if (!response.getStatusCode().isError()) {
            String key = emailService.sendMail(email);
            session.setAttribute("key", key);
            session.setMaxInactiveInterval((int) TimeUnit.MINUTES.toSeconds(10));
        }
        return response;
    }

    @GetMapping("/email/authentication/{authentication}")
    public ResponseEntity<String> authenticationCheck(@PathVariable String authentication, HttpSession session) {
        String key = (String) session.getAttribute("key");

        if (Objects.isNull(key))
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("인증 시간이 지났습니다. 다시 인증하기 버튼을 눌러주세요.");

        if (key.equals(authentication)) {
            session.invalidate();
            return ResponseEntity.status(HttpStatus.OK).body("성공");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 번호가 맞지 않습니다");
    }

    @GetMapping("/search/posts/{strNum}")
    public String searchPosts(HttpSession session, @ModelAttribute("dto") ConditionalSearchPostRequestDto dto, @PathVariable int strNum, Model model) {
        //TODO 이거 해야함
        PageMoveWithMessage pmwm = companyService.searchPosts(session, dto, strNum, strNum + 4);
        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        model.addAttribute("dto", dto);
        model.addAttribute("postPage", pmwm.getData());
        model.addAttribute("jobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("stacks", allJobsAndStacks.get("stacks"));
        return pmwm.getPageName();
    }

    @GetMapping("/search/posts/total/{strNum}")
    public String searchTotalPosts(HttpSession session, @RequestParam String search, @PathVariable int strNum, Model model) {
        PageMoveWithMessage pmwm = companyService.searchTotalPosts(session, search, strNum, strNum + 4);
        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        model.addAttribute("postPage", pmwm.getData());
        model.addAttribute("jobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("stacks", allJobsAndStacks.get("stacks"));
        return pmwm.getPageName();
    }

    @ModelAttribute("dto")
    public ConditionalSearchPostRequestDto setupModel(@RequestParam(name = "jobIdSet", required = false) Set<Long> jobIdSet,
                                                      @RequestParam(name = "stackIdSet", required = false) Set<Long> stackIdSet,
                                                      @RequestParam(name = "career", required = false, defaultValue = "0") Integer career,
                                                      @RequestParam(name = "includeEnded", required = false, defaultValue = "false") Boolean includeEnded,
                                                      @RequestParam(name = "addressSet", required = false) Set<String> addressSet
    ) {
        if (Objects.isNull(jobIdSet)) {
            jobIdSet = new HashSet<>();
        }

        if (Objects.isNull(stackIdSet)) {
            stackIdSet = new HashSet<>();
        }

        if (Objects.isNull(addressSet)) {
            addressSet = new HashSet<>();
        }

        return ConditionalSearchPostRequestDto.builder()
                .jobIdSet(jobIdSet)
                .addressSet(addressSet)
                .career(career)
                .stackIdSet(stackIdSet)
                .includeEnded(includeEnded)
                .build();
    }

    @GetMapping("/click/post/{postId}/detail")
    public String clickPostDetail(HttpSession session, @PathVariable Long postId, @RequestParam(required = false) Long companyId, @RequestParam(required = false) String postType, @RequestParam(required = false) String errorMessage, Model model) {
        PageMoveWithMessage postInfo = companyService.formPost(session, postType, companyId);
        PageMoveWithMessage postDetail = companyService.getPostDetail(session, postId, postType, companyId);

        PostResponseDto data = (PostResponseDto) postDetail.getData();

        List<JobResponseDto> jobs = adminService.getJobs(session, data.getJobIdSet());
        List<StackResponseDto> stacks = adminService.getStacks(session, data.getStackIdSet());

        Long userId = (Long) session.getAttribute("id");
        if (Objects.nonNull(userId)) {
            ApplicationLetterResponseDto apply = userService.applyPopup(session, userId);
            model.addAttribute("resumeList", apply.getResumeList());
            model.addAttribute("coverLetterList", apply.getCoverLetterList());
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("postId", postId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("info", postInfo.getData());
        model.addAttribute("detail", postDetail.getData());
        model.addAttribute("jobs", jobs);
        model.addAttribute("stacks", stacks);
        return "guest/post-detail";
    }


}

package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.CompanyCheckBnoRequestDto;
import com.miracle.memberservice.dto.request.CompanyJoinDto;
import com.miracle.memberservice.dto.request.LoginDto;
import com.miracle.memberservice.dto.request.UserJoinDto;
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

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import java.util.Objects;
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
    public String index() {
        return "index";
    }

    //회원가입 로그인 폼
    @GetMapping("/user/login-form")
    public String userLoginForm() {
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
    public String userLogin(@ModelAttribute LoginDto loginDto, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = userService.login(loginDto, session);
        String pageName = pmwm.getPageName();

        if (pmwm.getId() != null) {
            session.setAttribute("id", pmwm.getId());
            session.setAttribute("email", pmwm.getEmail());
            session.setAttribute("name", pmwm.getNameOrBno());
        }

        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pageName;
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


}

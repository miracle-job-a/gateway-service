package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.CompanyCheckBnoRequestDto;
import com.miracle.memberservice.dto.request.CompanyJoinDto;
import com.miracle.memberservice.dto.request.LoginDto;
import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.TempKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
@RequestMapping("/v1")
@RequiredArgsConstructor
public class GuestController {

    private final UserService userService;
    private final CompanyService companyService;
    private final JavaMailSender emailSender;

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

    @GetMapping("/user/email/duplicate/{email}")
    public ResponseEntity<String> userEmailDuplicationCheck(@PathVariable String email, HttpSession session) {
        ResponseEntity<String> response = userService.duplicateEmail(session, email);

        if (!response.getStatusCode().isError()) {
            String key = TempKey.make();
            sendEmailToUser(email, "Job-a 회원가입 인증번호", "인증번호는 " + key + "입니다.");
            session.setAttribute("key", key);
            session.setMaxInactiveInterval((int) TimeUnit.MINUTES.toSeconds(10));
        }
        return response;
    }

    @GetMapping("/company/email/duplicate/{email}")
    public ResponseEntity<String> companyEmailDuplicationCheck(@PathVariable String email, HttpSession session) {
        ResponseEntity<String> response = companyService.duplicateEmail(session, email);
        //TODO 제대로 만들기
        return response;
    }

    @GetMapping("")
    public ResponseEntity<String> authenticationCheck(){
        return null;
    }

    private void sendEmailToUser(String userEmail, String subject, String text) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(text);

            emailSender.send(message);
        } catch (MessagingException e) {
            // 예외 처리
        }
    }

}

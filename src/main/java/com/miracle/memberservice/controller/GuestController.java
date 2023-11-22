package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.CompanyCheckBnoRequestDto;
import com.miracle.memberservice.dto.request.CompanyJoinDto;
import com.miracle.memberservice.dto.request.LoginDto;
import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/v1")
@RequiredArgsConstructor
public class GuestController {

    private final UserService userService;
    private final CompanyService companyService;

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
        log.info(bno.toString());

        return companyService.bnoCertify(bno, session);

    }

    // 회원가입 API
    @PostMapping("/user/join")
    public String userJoin(@ModelAttribute UserJoinDto userJoinDto, Model model, HttpSession session) {
        PageMoveWithMessage pageMoveWithMessage = userService.join(userJoinDto, session);
        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageMoveWithMessage.getPageName();
    }

    @PostMapping("/company/join")
    public String companyJoin(@ModelAttribute CompanyJoinDto companyJoinDto, Model model, HttpSession session) {
        PageMoveWithMessage pageMoveWithMessage = companyService.join(companyJoinDto, session);
        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageMoveWithMessage.getPageName();
    }

    //로그인 API
    @PostMapping("/company/login")
    public String companyLogin(@ModelAttribute LoginDto loginDto, Model model, HttpSession session) {
        PageMoveWithMessage pageMoveWithMessage = companyService.login(loginDto, session);
        String pageName = pageMoveWithMessage.getPageName();

        if (pageName.equals("index")) {
            session.setAttribute(loginDto.getMemberType()+"Id", pageMoveWithMessage.getId());
            session.setAttribute("email", loginDto.getEmail());
        }

        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageName;
    }

    @PostMapping("/user/login")
    public String userLogin(@ModelAttribute LoginDto loginDto, Model model, HttpSession session) {
        PageMoveWithMessage pageMoveWithMessage = userService.login(loginDto, session);
        String pageName = pageMoveWithMessage.getPageName();

        if (pageName.equals("index")) {
            session.setAttribute(loginDto.getMemberType()+"Id", pageMoveWithMessage.getId());
            session.setAttribute("email", loginDto.getEmail());
        }

        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageName;
    }

}

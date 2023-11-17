package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.CompanyJoinDto;
import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.dto.request.UserLoginDto;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
    public String bnoCertify(@RequestParam String bno) {
        log.info(bno);

        return null;
    }

    // 회원가입 API
    @PostMapping("/user/join")
    public String userJoin(@ModelAttribute UserJoinDto userJoinDto, Model model, HttpSession session) {
        PageMoveWithMessage pageMoveWithMessage = userService.userJoin(userJoinDto, session);
        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageMoveWithMessage.getPageName();
    }

    @PostMapping("/company/join")
    public String companyJoin(@ModelAttribute CompanyJoinDto companyJoinDto, Model model, HttpSession session) {
        PageMoveWithMessage pageMoveWithMessage = companyService.companyJoin(companyJoinDto, session);
        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageMoveWithMessage.getPageName();
    }

    //로그인 API
    @PostMapping("/user/login")
    public String userLogin(@ModelAttribute UserLoginDto userLoginDto, Model model, HttpSession session) {
        PageMoveWithMessage pageMoveWithMessage = userService.userLogin(userLoginDto, session);
        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageMoveWithMessage.getPageName();
    }

}

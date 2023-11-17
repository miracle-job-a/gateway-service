package com.miracle.memberservice.controller.json;

import com.miracle.memberservice.dto.request.CompanyJoinDto;
import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequestMapping("/v1")
@RequiredArgsConstructor
public class JsonController {

    private final UserService userService;
    private final CompanyService companyService;

    @PostMapping(value = "/company/bno/")
    public String bnoCertify(@RequestParam String bno) {
        log.info(bno);

        return null;
    }

    // 회원가입 버튼 구현
    @PostMapping("/user/join")
    public String userJoin(@ModelAttribute UserJoinDto userJoinDto, Model model, HttpSession session) {
        String sessionId = session.getId();
        PageMoveWithMessage pageMoveWithMessage = userService.userJoin(userJoinDto, sessionId);
        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageMoveWithMessage.getPageName();
    }

    @PostMapping("/company/join")
    public String companyJoin(@ModelAttribute CompanyJoinDto companyJoinDto, Model model, HttpSession session) {
        String sessionId = session.getId();
        PageMoveWithMessage pageMoveWithMessage = companyService.companyJoin(companyJoinDto, sessionId);
        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageMoveWithMessage.getPageName();
    }
}

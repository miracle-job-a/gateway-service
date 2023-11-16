package com.miracle.memberservice.controller.json;

import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/v1")
@RequiredArgsConstructor
public class JsonController {

    private final UserService userService;

    @PostMapping(value = "/company/bno")
    public String bnoCertify(@RequestParam String bno) {
        log.info(bno);

        return bno;
    }

    // 회원가입 버튼 구현
    @PostMapping("/user/join")
    public String userJoin(@ModelAttribute UserJoinDto userJoinDto, Model model) {
        PageMoveWithMessage pageMoveWithMessage = userService.userJoin(userJoinDto);
        model.addAttribute("errorMessage", pageMoveWithMessage.getErrorMessage());
        return pageMoveWithMessage.getPageName();
    }

    @PostMapping("/company/join")
    public String companyJoin() {
        return null;
    }
}

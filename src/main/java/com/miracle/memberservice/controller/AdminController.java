package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.response.CompanyListResponseDto;
import com.miracle.memberservice.dto.response.UserListResponseDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/login")
    private String adminLogin(){ return "admin/login"; }
    @GetMapping("/user/list/{strNum}/{endNum}")
    public String getUserList(@PathVariable int strNum, @PathVariable int endNum, HttpSession session, Model model) {
        PageMoveWithMessage pmwm = adminService.getUserList(session, strNum, endNum);
        List<UserListResponseDto> data = (List<UserListResponseDto>) pmwm.getData();
        System.out.println("AdminController data : " + data);
        model.addAttribute("strNum", strNum);
        model.addAttribute("endNum", endNum);
        model.addAttribute("listPage", data);
        return pmwm.getPageName();
    }

    @GetMapping("/company/list/{strNum}/{endNum}")
    public String getCompanyList(@PathVariable int strNum, @PathVariable int endNum, HttpSession session, Model model) {
        PageMoveWithMessage pmwm = adminService.getCompanyList(session, strNum, endNum);
        List<CompanyListResponseDto> data = (List<CompanyListResponseDto>) pmwm.getData();
        System.out.println("AdminController getCompanyList data : " + data);
        System.out.println("AdminController getCompanyList strNum : " + strNum);
        System.out.println("AdminController getCompanyList endNum : " + endNum);
        model.addAttribute("strNum", strNum);
        model.addAttribute("endNum", endNum);
        model.addAttribute("listPage", data);
        return pmwm.getPageName();
    }

    @GetMapping("/company")
    private String userList(){ return "admin/companyList"; }

    @GetMapping("/stacks")
    private String stackList(){ return "admin/stackList"; }
    @GetMapping("/jobs")
    private String jobList(){ return "admin/jobList"; }

}

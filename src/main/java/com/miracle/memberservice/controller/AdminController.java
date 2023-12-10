package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.response.CompanyListResponseDto;
import com.miracle.memberservice.dto.response.UserListResponseDto;
import com.miracle.memberservice.dto.response.StackAndJobResponseDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final CompanyService companyService;

    @GetMapping("/login")
    private String adminLogin(){ return "admin/login"; }

    @GetMapping("/user/list/{strNum}/{endNum}")
    public String getUserList(@PathVariable int strNum, @PathVariable int endNum, HttpSession session, Model model) {
        PageMoveWithMessage pmwm = adminService.getUserList(session, strNum, endNum);
        List<UserListResponseDto> data = (List<UserListResponseDto>) pmwm.getData();
        model.addAttribute("strNum", strNum);
        model.addAttribute("endNum", endNum);
        model.addAttribute("listPage", data);
        return pmwm.getPageName();
    }

    @GetMapping("/company/list/{strNum}/{endNum}")
    public String getCompanyList(@PathVariable int strNum, @PathVariable int endNum, HttpSession session, Model model) {
        PageMoveWithMessage pmwm = adminService.getCompanyList(session, strNum, endNum);
        List<CompanyListResponseDto> data = (List<CompanyListResponseDto>) pmwm.getData();

        model.addAttribute("strNum", strNum);
        model.addAttribute("endNum", endNum);
        model.addAttribute("listPage", data);
        return pmwm.getPageName();
    }

    @GetMapping("/company")
    private String userList(){ return "admin/companyList"; }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/v1";
    }

    @GetMapping
    public String main(HttpSession session) {
        return "admin/main";
    }

    @GetMapping("/stacks")
    private String stackList(HttpSession session, Model model){
        PageMoveWithMessage pmwm = adminService.getAllStack(session);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalStackList", data);
        return pmwm.getPageName();
    }

    @PostMapping("/register/stack")
    private String registerStack(@RequestParam String stackName, Model model, HttpSession session){
        PageMoveWithMessage pmwm = adminService.registerStack(session, stackName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalStackList", data);
        return pmwm.getPageName();
    }

    @PostMapping("/stacks")
    private String modifyStack(@RequestParam String stackId, @RequestParam String modifiedName, Model model, HttpSession session){
        PageMoveWithMessage pmwm = adminService.modifyStack(session, stackId, modifiedName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();
        String errorMessage = pmwm.getErrorMessage();
        model.addAttribute("totalStackList", data);
        model.addAttribute("errorMessage", errorMessage);
        return pmwm.getPageName();
    }

    @GetMapping("/search/stack")
    private String searchStack(@RequestParam(required = false) String stackName, Model model, HttpSession session){
        PageMoveWithMessage pmwm = adminService.searchStack(session, stackName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalStackList", data);
        return pmwm.getPageName();
    }

    @GetMapping("/jobs")
    private String jobList(HttpSession session, Model model){
        PageMoveWithMessage pmwm = adminService.getAllJob(session);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalJobList", data);
        return pmwm.getPageName();
    }

    @PostMapping("/register/job")
    private String registerJob(@RequestParam String jobName, Model model, HttpSession session){
        PageMoveWithMessage pmwm = adminService.registerJob(session, jobName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalJobList", data);
        return pmwm.getPageName();
    }

    @PostMapping("/jobs")
    private String modifyJob(@RequestParam String jodId, @RequestParam String modifiedName, Model model, HttpSession session){
        PageMoveWithMessage pmwm = adminService.modifyJob(session, jodId, modifiedName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalJobList", data);
        return pmwm.getPageName();
    }

    @GetMapping("/search/job")
    private String searchJob(@RequestParam(required = false) String jobName, Model model, HttpSession session){
        PageMoveWithMessage pmwm = adminService.searchJob(session, jobName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalJobList", data);
        return pmwm.getPageName();
    }

    @GetMapping("/approval/{companyId}")
    public String approveCompany(@PathVariable String companyId, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.approveCompany(session, companyId);
        return pmwm.getPageName();
    }
}

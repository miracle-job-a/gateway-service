package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.response.CompanyListResponseDto;
import com.miracle.memberservice.dto.response.ManagePostsResponseDto;
import com.miracle.memberservice.dto.response.StackAndJobResponseDto;
import com.miracle.memberservice.dto.response.UserJoinListResponseDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final CompanyService companyService;
    private final UserService userService;

    @GetMapping("/login")
    private String adminLogin() {
        return "admin/login";
    }

    @GetMapping("/user/list/{strNum}/{endNum}")
    public String getUserList(@PathVariable int strNum, @PathVariable int endNum, HttpSession session, Model model) {
        PageMoveWithMessage pmwm = adminService.getUserList(session, strNum, endNum);
        List<UserJoinListResponseDto> data = (List<UserJoinListResponseDto>) pmwm.getData();
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

    @GetMapping("/user/join-count")
    public String getUserJoinCountByDay(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year,
            @RequestParam(name = "month", required = false, defaultValue = "0") int month,
            HttpSession session, Model model) {

        if (year == 0) year = LocalDate.now().getYear();
        if (month == 0) month = LocalDate.now().getMonthValue();

        PageMoveWithMessage pmwm = userService.getUserJoinCountByDay(session, year, month);
        model.addAttribute("chartData", pmwm.getData());
        model.addAttribute("year", year);
        model.addAttribute("month", month);

        return pmwm.getPageName();
    }

    @ResponseBody
    @GetMapping("/user/join-count/ajax")
    public ResponseEntity<Map<String, Object>> getReloadUserJoinCountByDay(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year,
            @RequestParam(name = "month", required = false, defaultValue = "0") int month,
            HttpSession session) {
        PageMoveWithMessage pmwm = userService.getUserJoinCountByDay(session, year, month);

        Map<String, Object> map = new HashMap<>();
        map.put("chartData", pmwm.getData());

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/company/join-count")
    public String getCompanyJoinCountByDay(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year,
            @RequestParam(name = "month", required = false, defaultValue = "0") int month,
            HttpSession session, Model model) {

        if (year == 0) year = LocalDate.now().getYear();
        if (month == 0) month = LocalDate.now().getMonthValue();

        PageMoveWithMessage pmwm = companyService.getCompanyJoinCountByDay(session, year, month);

        model.addAttribute("chartData", pmwm.getData());
        model.addAttribute("year", year);
        model.addAttribute("month", month);

        return pmwm.getPageName();
    }

    @ResponseBody
    @GetMapping("/company/join-count/ajax")
    public ResponseEntity<Map<String, Object>> getReloadCompanyJoinCountByDay(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year,
            @RequestParam(name = "month", required = false, defaultValue = "0") int month,
            HttpSession session) {

        PageMoveWithMessage pmwm = companyService.getCompanyJoinCountByDay(session, year, month);

        Map<String, Object> map = new HashMap<>();
        map.put("chartData", pmwm.getData());

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/join-count")
    public String getJoinCountByDay(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year,
            @RequestParam(name = "month", required = false, defaultValue = "0") int month,
            HttpSession session, Model model) {

        if (year == 0) year = LocalDate.now().getYear();
        if (month == 0) month = LocalDate.now().getMonthValue();

        PageMoveWithMessage pmwm1 = userService.getUserJoinCountByDay(session, year, month);
        PageMoveWithMessage pmwm2 = companyService.getCompanyJoinCountByDay(session, year, month);

        model.addAttribute("userChartData", pmwm1.getData());
        model.addAttribute("companyChartData", pmwm2.getData());
        model.addAttribute("year", year);
        model.addAttribute("month", month);

        return "admin/join-count";
    }

    @ResponseBody
    @GetMapping("/join-count/ajax")
    public ResponseEntity<Map<String, Object>> getReloadJoinCountByDay(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year,
            @RequestParam(name = "month", required = false, defaultValue = "0") int month,
            HttpSession session) {

        PageMoveWithMessage pmwm1 = userService.getUserJoinCountByDay(session, year, month);
        PageMoveWithMessage pmwm2 = companyService.getCompanyJoinCountByDay(session, year, month);

        Map<String, Object> map = new HashMap<>();
        map.put("userChartData", pmwm1.getData());
        map.put("companyChartData", pmwm2.getData());

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/v1";
    }

    @GetMapping
    public String main(HttpSession session) {
        return "admin/main";
    }

    @GetMapping("/stacks")
    private String getStackList(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = adminService.getAllStack(session);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalStackList", data);
        return pmwm.getPageName();
    }

    @PostMapping("/register/stack")
    private String registerStack(@RequestParam String stackName, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = adminService.registerStack(session, stackName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalStackList", data);
        return pmwm.getPageName();
    }

    @PostMapping("/stacks")
    private String modifyStack(@RequestParam String stackId, @RequestParam String modifiedName, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = adminService.modifyStack(session, stackId, modifiedName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();
        String errorMessage = pmwm.getErrorMessage();
        model.addAttribute("totalStackList", data);
        model.addAttribute("errorMessage", errorMessage);
        return pmwm.getPageName();
    }

    @GetMapping("/search/stack")
    private String searchStack(@RequestParam(required = false) String stackName, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = adminService.searchStack(session, stackName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalStackList", data);
        return pmwm.getPageName();
    }

    @GetMapping("/jobs")
    private String getJobList(HttpSession session, Model model) {
        PageMoveWithMessage pmwm = adminService.getAllJob(session);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalJobList", data);
        return pmwm.getPageName();
    }

    @PostMapping("/register/job")
    private String registerJob(@RequestParam String jobName, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = adminService.registerJob(session, jobName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalJobList", data);
        return pmwm.getPageName();
    }

    @PostMapping("/jobs")
    private String modifyJob(@RequestParam String jodId, @RequestParam String modifiedName, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = adminService.modifyJob(session, jodId, modifiedName);
        List<StackAndJobResponseDto> data = (List<StackAndJobResponseDto>) pmwm.getData();

        model.addAttribute("totalJobList", data);
        return pmwm.getPageName();
    }

    @GetMapping("/search/job")
    private String searchJob(@RequestParam(required = false) String jobName, Model model, HttpSession session) {
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

    @GetMapping("/posts/today")
    public String getTodayPostCount(@RequestParam(name = "year", required = false, defaultValue = "0") int year,
                                    @RequestParam(name = "month", required = false, defaultValue = "0") int month,
                                    HttpSession session, Model model) {
        if (year == 0) year = LocalDate.now().getYear();
        if (month == 0) month = LocalDate.now().getMonthValue();
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        PageMoveWithMessage pmwm = companyService.getTodayPostCount(year, month, session);
        model.addAttribute("chartData", pmwm.getData());
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("daysInMonth", daysInMonth);
        return pmwm.getPageName();
    }

    @ResponseBody
    @GetMapping("/posts/day")
    public ResponseEntity<Map<String, Object>> getReloadTodayPostCount(@RequestParam(name = "year") int year, @RequestParam(name = "month") int month, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.getTodayPostCount(year, month, session);
        Map<String, Object> map = new HashMap<>();
        map.put("chartData", pmwm.getData());
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/post/popular")
    public String getPopularPosts(HttpSession session, Model model) {
        PageMoveWithMessage companyListResult = adminService.getCompanyList(session, 1, 5);
        List<List<CompanyListResponseDto>> companyList = (List<List<CompanyListResponseDto>>) companyListResult.getData();

        List<ManagePostsResponseDto> allPost = new ArrayList<>();

        for (List<CompanyListResponseDto> companies : companyList) {
            for (CompanyListResponseDto company : companies) {
                PageMoveWithMessage postListResult = companyService.postList(session, company.getId(), 1, 10, "latest");

                List<List<ManagePostsResponseDto>> postListData = (List<List<ManagePostsResponseDto>>) postListResult.getData();
                List<ManagePostsResponseDto> postList = postListData.stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                allPost.addAll(postList);
            }
        }

        List<ManagePostsResponseDto> popularPosts = allPost.stream()
                .sorted(Comparator.comparingInt(ManagePostsResponseDto::getApplicant).reversed())
                .limit(3)
                .collect(Collectors.toList());

        model.addAttribute("chartData", popularPosts);

        return "admin/post-popular";
    }
}

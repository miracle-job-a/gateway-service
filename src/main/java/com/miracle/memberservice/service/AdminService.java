package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.JobRequestDto;
import com.miracle.memberservice.dto.request.LoginDto;
import com.miracle.memberservice.dto.request.StackRequestDto;
import com.miracle.memberservice.dto.response.*;
import com.miracle.memberservice.util.ApiResponseToList;
import com.miracle.memberservice.util.Const;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class AdminService {
    public PageMoveWithMessage login(LoginDto loginDto, HttpSession session) {
        ApiResponse response = ServiceCall.post(session, loginDto, loginDto.getMemberType(), "/admin/login");
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("guest/admin-login", response.getMessage());

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        Long id = data.get("id") instanceof Integer ? ((Integer) data.get("id")).longValue() : (Long) data.get("id");

        AdminLoginResponseDto dto = AdminLoginResponseDto.builder()
                .id(id)
                .email((String) data.get("email"))
                .build();

        return new PageMoveWithMessage("admin/main", dto);
    }

    public Map<String, List<?>> getAllJobsAndStacks(HttpSession session) {
        ApiResponse response = ServiceCall.get(session, "admin", "/admin/jobstacks");

        if(response.getHttpStatus()!=200) return null;

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        List<StackResponseDto> stacks = ApiResponseToList.stacks(data.get("stacks"));
        List<JobResponseDto> jobs = ApiResponseToList.jobs(data.get("jobs"));

        Map<String, List<?>> stacksAndJobs = new HashMap<>();

        stacksAndJobs.put("stacks", stacks);
        stacksAndJobs.put("jobs", jobs);

        return stacksAndJobs;
    }

    public List<JobResponseDto> getJobs(HttpSession session, List<Integer> jobs){
        ApiResponse response = ServiceCall.post(session, new JobRequestDto(jobs), Const.RequestHeader.ADMIN, "/admin/jobs");
        if (response.getData() instanceof Boolean) return null;

        return ApiResponseToList.jobs(response.getData());
    }

    public List<StackResponseDto> getStacks(HttpSession session, List<Integer> stacks){
        ApiResponse response = ServiceCall.post(session, new StackRequestDto(stacks), Const.RequestHeader.ADMIN, "/admin/stacks");
        if (response.getData() instanceof Boolean) return null;

        return ApiResponseToList.stacks(response.getData());
    }

    public List<JobResponseDto> getAllJobs(HttpSession session){
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.ADMIN, "/admin/jobs");
        return ApiResponseToList.jobs(response.getData());
    }

    public PageMoveWithMessage getAllStack(HttpSession session){
//        Long adminId = (Long) session.getAttribute("id");

         ApiResponse response = ServiceCall.get(session, Const.RequestHeader.ADMIN, "/admin/stacks");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("admin/main", response.getMessage());

        List<StackAndJobResponseDto> dtos = ApiResponseToList.stackList(response.getData());
        System.out.println(dtos);
        return new PageMoveWithMessage("admin/stackList", dtos);
    }
}

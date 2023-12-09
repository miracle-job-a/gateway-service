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

    public PageMoveWithMessage getUserList(HttpSession session, int strNum, int endNum) {
        ApiResponse response = ServiceCall.getParamList(session, Const.RequestHeader.USER, "/user", strNum, endNum);
        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("index", response.getMessage());
        List<List<UserListResponseDto>> userList = ApiResponseToList.userList(response.getData());

        return new PageMoveWithMessage("admin/userList", userList);
    }

    public PageMoveWithMessage getCompanyList(HttpSession session, int strNum, int endNum) {
        ApiResponse response = ServiceCall.getParamList(session, Const.RequestHeader.COMPANY, "/company/list", strNum, endNum, false);
        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("index", response.getMessage());
        List<List<CompanyListResponseDto>> companyList = ApiResponseToList.companyList(response.getData());

        return new PageMoveWithMessage("admin/companyList", companyList);
    }

    public PageMoveWithMessage getAllJob(HttpSession session){
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.ADMIN, "/admin/jobs");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("admin/main", response.getMessage());

        List<StackAndJobResponseDto> dtos = ApiResponseToList.stackAndJobList(response.getData());
        return new PageMoveWithMessage("admin/jobList", dtos);
    }

    public PageMoveWithMessage registerJob(HttpSession session, String jobName){
        ApiResponse response = ServiceCall.getParamJobName(session, Const.RequestHeader.ADMIN, "/admin/add", jobName);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("admin/main", response.getMessage());

        return new PageMoveWithMessage("redirect:/v1/admin/jobs", response.getMessage());
    }

    public PageMoveWithMessage modifyJob(HttpSession session, String jodId, String modifiedName){

        ApiResponse response = ServiceCall.putModifyJobParam(session, Const.RequestHeader.ADMIN, "/admin/edit", jodId, modifiedName);

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("admin/main", response.getMessage());

        List<StackAndJobResponseDto> dtos = ApiResponseToList.stackAndJobList(response.getData());
        return new PageMoveWithMessage("redirect:/v1/admin/jobs", dtos);
    }

    public PageMoveWithMessage searchJob(HttpSession session, String jobName) {
        ApiResponse response = ServiceCall.getParamJobName(session, Const.RequestHeader.ADMIN, "/admin/search", jobName);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("admin/main", response.getMessage());
        List<StackAndJobResponseDto> dtos = ApiResponseToList.stackAndJobList(response.getData());
        return new PageMoveWithMessage("admin/jobList", dtos);
    }

    public PageMoveWithMessage getAllStack(HttpSession session){
         ApiResponse response = ServiceCall.get(session, Const.RequestHeader.ADMIN, "/admin/stacks");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("admin/main", response.getMessage());

        List<StackAndJobResponseDto> dtos = ApiResponseToList.stackAndJobList(response.getData());
        return new PageMoveWithMessage("admin/stackList", dtos);
    }

    public PageMoveWithMessage registerStack(HttpSession session, String stackName){
        ApiResponse response = ServiceCall.getParamStackName(session, Const.RequestHeader.ADMIN, "/admin/add", stackName);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("admin/main", response.getMessage());

        return new PageMoveWithMessage("redirect:/v1/admin/stacks", response.getMessage());
    }

    public PageMoveWithMessage modifyStack(HttpSession session, String stackId, String modifiedName){

        ApiResponse response = ServiceCall.putModifyParam(session, Const.RequestHeader.ADMIN, "/admin/edit", stackId, modifiedName);

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("admin/main", response.getMessage());

        List<StackAndJobResponseDto> dtos = ApiResponseToList.stackAndJobList(response.getData());
        return new PageMoveWithMessage("redirect:/v1/admin/stacks", dtos);
    }

    public PageMoveWithMessage searchStack(HttpSession session, String stackName) {
        ApiResponse response = ServiceCall.getParamStackName(session, Const.RequestHeader.ADMIN, "/admin/search", stackName);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("admin/main", response.getMessage());
        List<StackAndJobResponseDto> dtos = ApiResponseToList.stackAndJobList(response.getData());
        return new PageMoveWithMessage("admin/stackList", dtos);
    }
}

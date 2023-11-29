package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.JobResponseDto;
import com.miracle.memberservice.dto.response.StackResponseDto;
import com.miracle.memberservice.util.ApiResponseToList;
import com.miracle.memberservice.util.ServiceCall;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class AdminService {
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
}

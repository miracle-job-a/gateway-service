package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.ApplicationLetterListResponseDto;
import com.miracle.memberservice.dto.response.ResumeInApplicationLetterResponseDto;
import com.miracle.memberservice.util.ApiResponseToList;
import com.miracle.memberservice.util.Const;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Slf4j
public class MyPageService {

    // 지원 이력서 조회하기
    public PageMoveWithMessage resumeInApplicationLetterDetail(HttpSession session, Long applicationLetterId){
        Long userId = (Long) session.getAttribute("id");

        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/"+userId+"/application-letter/"+applicationLetterId+"/resume");

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        ResumeInApplicationLetterResponseDto dto = ResumeInApplicationLetterResponseDto.builder()
                .resumeTitle((String) data.get("resumeTitle"))
                .userName((String) data.get("userName"))
                .userEmail((String) data.get("userEmail"))
                .userCareer((Integer) data.get("userCareer"))
                .userBirth((String) data.get("userBirth"))
                .userPhone((String) data.get("userPhone"))
                .userAddress((String) data.get("userAddress"))
                .userJob((String) data.get("userJob"))
                .userStackIdSet((ArrayList<Integer>) data.get("userStackIdSet"))
                .userEducation((String) data.get("userEducation"))
                .userGitLink((String) data.get("userGitLink"))
                .userCareerDetailList((List<String>) data.get("userCareerDetailList"))
                .userProjectList((List<String>) data.get("userProjectList"))
                .userEtcList((List<String>) data.get("userEtcList"))
                .build();

        return new PageMoveWithMessage("user/submitted-resume", dto);
    }

    // 지원현황 목록 불러오기
    public PageMoveWithMessage applicationLetterList(HttpSession session, int startPage, int endPage, String pageSize){
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.getParamList(session, "user", "/user/"+userId+"/application-letter", startPage, endPage, pageSize);

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("/v1", response.getMessage());

        List<List<ApplicationLetterListResponseDto>> letter = ApiResponseToList.applicationLetterList(response.getData());
        return new PageMoveWithMessage("user/apply-list", letter);
    }
}

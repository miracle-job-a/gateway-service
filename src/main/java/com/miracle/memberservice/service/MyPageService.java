package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.ResumeInApplicationLetterResponseDto;
import com.miracle.memberservice.util.Const;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class MyPageService {

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

    /*
    *
    * toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
	*
	* DB_password=5002;DB_url=jdbc:mysql://localhost:3306/miracle_user;DB_username=root
	*
	* */
}

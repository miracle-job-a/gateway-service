package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ResumeInApplicationLetterResponseDto {

    private final String resumeTitle;
    private final String userName;
    private final String userEmail;
    private final int userCareer;
    private final String userBirth;
    private final String userPhone;
    private final String userAddress;
    private final String userJob;
    private final ArrayList<Integer> userStackIdSet;
    private final String userEducation;
    private final String userGitLink;
    private final List<String> userCareerDetailList;
    private final List<String> userProjectList;
    private final List<String> userEtcList;

    @Builder
    public ResumeInApplicationLetterResponseDto(String resumeTitle, String userName, String userEmail, int userCareer, String userBirth, String userPhone, String userAddress, String userJob, ArrayList<Integer> userStackIdSet, String userEducation, String userGitLink, List<String> userCareerDetailList, List<String> userProjectList, List<String> userEtcList) {
        this.resumeTitle = resumeTitle;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userCareer = userCareer;
        this.userBirth = userBirth;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userJob = userJob;
        this.userStackIdSet = userStackIdSet;
        this.userEducation = userEducation;
        this.userGitLink = userGitLink;
        this.userCareerDetailList = userCareerDetailList;
        this.userProjectList = userProjectList;
        this.userEtcList = userEtcList;
    }
}

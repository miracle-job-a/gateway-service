package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ResumeResponseDto {
    private final Long id;
    private final String title;
    private final String photo;
    private final int career;
    private final String birth;
    private final String phone;
    private final String education;
    private final String gitLink;
    private final ArrayList<Integer> jobIdSet;
    private final ArrayList<Integer> stackIdSet;
    private final List<String> careerDetailList;
    private final List<String> projectList;
    private final List<String> etcList;

    @Builder
    public ResumeResponseDto(Long id, String title, String photo, int career, String birth, String phone, String education, String gitLink, ArrayList<Integer> jobIdSet, ArrayList<Integer> stackIdSet, List<String> careerDetailList, List<String> projectList, List<String> etcList) {
        this.id = id;
        this.title = title;
        this.photo = photo;
        this.career = career;
        this.birth = birth;
        this.phone = phone;
        this.education = education;
        this.gitLink = gitLink;
        this.jobIdSet = jobIdSet;
        this.stackIdSet = stackIdSet;
        this.careerDetailList = careerDetailList;
        this.projectList = projectList;
        this.etcList = etcList;
    }
}

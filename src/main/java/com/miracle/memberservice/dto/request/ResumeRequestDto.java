package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ToString
@Getter
public class ResumeRequestDto {

    private final String title;
    @Setter
    private String photo;
    private final int career;
    private final String education;
    private final String gitLink;
    private final String open;
    private final Set<Long> jobIdSet;
    private final Set<Long> stackIdSet;
    private final List<String> careerDetailList;
    private final List<String> projectList;
    private final List<String> etcList;
    private final Long postId;
    private final Long companyId;
    private final String postType;

    public ResumeRequestDto(String title, int career, String education, String gitLink, String open, Set<Long> jobIdSet, Set<Long> stackIdSet, List<String> careerDetailList, List<String> projectList, List<String> etcList, Long postId, Long companyId, String postType) {
        this.title = title;
        this.career = career;
        this.education = education;
        this.gitLink = gitLink;
        this.open = open;
        this.jobIdSet = jobIdSet;
        this.stackIdSet = stackIdSet;
        this.careerDetailList = isNullCollection(careerDetailList);
        this.projectList = isNullCollection(projectList);
        this.etcList = isNullCollection(etcList);
        this.postId = postId;
        this.companyId = companyId;
        this.postType = postType;
    }

    private List<String> isNullCollection(List<String> list){
        if(Objects.isNull(list)) return new ArrayList<>();
        return list;
    }
}

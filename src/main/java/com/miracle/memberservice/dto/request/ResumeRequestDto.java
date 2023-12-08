package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class ResumeRequestDto {

    private final String title;
    private final String photo;
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
}

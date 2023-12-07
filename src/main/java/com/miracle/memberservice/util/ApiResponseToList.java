package com.miracle.memberservice.util;

import com.miracle.memberservice.dto.request.JobRequestDto;
import com.miracle.memberservice.dto.response.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http11.filters.SavedRequestInputFilter;

import javax.servlet.http.HttpSession;
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ApiResponseToList {
    public static List<CompanyFaqResponseDto> faqList(Object object) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<CompanyFaqResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {

            dtos.add(CompanyFaqResponseDto.builder()
                    .id(lhm.get("id"))
                    .question(lhm.get("question"))
                    .answer(lhm.get("answer"))
                    .build());
        }
        return dtos;
    }

    /*public static List<InterviewResponseDto> interview(Object object) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<InterviewResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> item : data ){
            dtos.add(InterviewResponseDto.builder()
                    ..build())
        }
    }*/

    public static List<StackResponseDto> stacks(Object object) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<StackResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {

            Integer id = (Integer) lhm.get("id");
            dtos.add(StackResponseDto.builder()
                    .id(id.longValue())
                    .name((String) lhm.get("name"))
                    .build());
        }
        return dtos;
    }

    public static List<JobResponseDto> jobs(Object object) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<JobResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {

            Integer id = (Integer) lhm.get("id");
            dtos.add(JobResponseDto.builder()
                    .id(id.longValue())
                    .name((String) lhm.get("name"))
                    .build());
        }
        return dtos;
    }

    public static List<List<ManagePostsResponseDto>> postList(Object object, HttpSession session) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;
        List<List<ManagePostsResponseDto>> pageList = new ArrayList<>();

        for (LinkedHashMap<String, Object> lhm : data) {
            List<ManagePostsResponseDto> dtos = new ArrayList<>();
            Integer numberOfElements = (Integer) lhm.get("numberOfElements");
            if (numberOfElements > 0) {
                ArrayList<LinkedHashMap<String, Object>> content = (ArrayList<LinkedHashMap<String, Object>>) lhm.get("content");
                for (LinkedHashMap<String, Object> dto : content) {
                    Integer id = (Integer) dto.get("id");
                    Integer applicant = 0;
                    try {
                        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/post/" + id + "/applicant/num");
                        applicant = (Integer) response.getData();
                    } catch (ClassCastException e) {
                        log.error("지원 현황 없음");
                    }

                    dtos.add(ManagePostsResponseDto.builder()
                            .id(id.longValue())
                            .postType((String) dto.get("postType"))
                            .closed((Boolean) dto.get("closed"))
                            .createdAt(divideTime((String) dto.get("createdAt")))
                            .title((String) dto.get("title"))
                            .endDate(divideTime((String) dto.get("endDate")))
                            .applicant(applicant)
                            .build());
                }
                pageList.add(dtos);
            }
        }
        return pageList;
    }

    public static List<List<ConditionalSearchPostResponseDto>> searchPosts(Object object, HttpSession session) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;
        List<List<ConditionalSearchPostResponseDto>> pageList = new ArrayList<>();

        for (LinkedHashMap<String, Object> lhm : data) {
            List<ConditionalSearchPostResponseDto> dtos = new ArrayList<>();
            Integer numberOfElements = (Integer) lhm.get("numberOfElements");
            if (numberOfElements > 0) {
                ArrayList<LinkedHashMap<String, Object>> content = (ArrayList<LinkedHashMap<String, Object>>) lhm.get("content");
                for (LinkedHashMap<String, Object> dto : content) {
                    Integer id = (Integer) dto.get("id");
                    Integer companyId = (Integer) dto.get("companyId");
                    ArrayList<Integer> jobs = (ArrayList<Integer>) dto.get("jobIdSet");

                    ApiResponse response = ServiceCall.post(session, new JobRequestDto(jobs), Const.RequestHeader.ADMIN, "/admin/jobs");
                    String jobDetail;
                    if (response.getData() instanceof Boolean) {
                        jobDetail = null;
                    } else {
                        List<JobResponseDto> job = ApiResponseToList.jobs(response.getData());
                        jobDetail = job.get(0).getName();
                    }

                    dtos.add(ConditionalSearchPostResponseDto.builder()
                            .id(id.longValue())
                            .companyId(companyId.longValue())
                            .postType((String) dto.get("postType"))
                            .closed((Boolean) dto.get("closed"))
                            .title((String) dto.get("title"))
                            .endDate(divideTime((String) dto.get("endDate")))
                            .workAddress((String) dto.get("workAddress"))
                            .career((Integer) dto.get("career"))
                            .job(jobDetail)
                            .name((String) dto.get("name"))
                            .photo((String) dto.get("photo"))
                            .build());
                }
                pageList.add(dtos);
            }
        }
        return pageList;
    }

    public static List<QuestionResponseDto> questionList(Object object) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<QuestionResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {

            Integer id = (Integer) lhm.get("id");
            dtos.add(QuestionResponseDto.builder()
                    .id(id.longValue())
                    .question((String) lhm.get("question"))
                    .build());
        }
        return dtos;
    }

    public static List<ResumeListResponseDto> resumeList(Object object) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<ResumeListResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {

            Integer id = (Integer) lhm.get("id");
            dtos.add(ResumeListResponseDto.builder()
                    .id(id.longValue())
                    .title((String) lhm.get("title"))
                    .jobIdSet((ArrayList<Integer>) lhm.get("jobIdSet"))
                    .modifiedAt((String) lhm.get("modifiedAt"))
                    .open((Boolean) lhm.get("open"))
                    .build());
        }
        return dtos;
    }

    public static List<List<CoverLetterListResponseDto>> coverLetterList(Object object) {
        List<ArrayList<LinkedHashMap<String, Object>>> data = (ArrayList<ArrayList<LinkedHashMap<String, Object>>>) object;
        List<List<CoverLetterListResponseDto>> pageList = new ArrayList<>();

        for (ArrayList<LinkedHashMap<String, Object>> page : data) {
            List<CoverLetterListResponseDto> dtos = new ArrayList<>();
            if (!page.isEmpty()) {
                for (Map<String, Object> coverLetter : page) {
                    Integer id = (Integer) coverLetter.get("id");
                    dtos.add(CoverLetterListResponseDto.builder()
                            .id(id.longValue())
                            .title(String.valueOf(coverLetter.get("title")))
                            .modifiedAt(String.valueOf(coverLetter.get("modifiedAt")))
                            .build());
                }
                pageList.add(dtos);
            }
        }
        return pageList;
    }

    public static List<List<ApplicationLetterListResponseDto>> applicationLetterList(Object object) {
        List<ArrayList<LinkedHashMap<String, Object>>> data = (ArrayList<ArrayList<LinkedHashMap<String, Object>>>) object;
        List<List<ApplicationLetterListResponseDto>> pageList = new ArrayList<>();

        for (ArrayList<LinkedHashMap<String, Object>> page : data) {
            List<ApplicationLetterListResponseDto> dtos = new ArrayList<>();
            if (!page.isEmpty()) {
                for (Map<String, Object> letter : page) {
                    Integer applicationLetterId = (Integer) letter.get("applicationLetterId");
                    Integer postId = (Integer) letter.get("postId");
                    Integer interviewId = (Integer) letter.get("interviewId");
                    Long interviewIdValue = (interviewId != null) ? interviewId.longValue() : null;

                    dtos.add(ApplicationLetterListResponseDto.builder()
                            .applicationLetterId(applicationLetterId.longValue())
                            .postId(postId.longValue())
                            .interviewId(interviewIdValue)
                            .postType(String.valueOf(letter.get("postType")))
                            .submitDate(String.valueOf(letter.get("submitDate")))
                            .applicationStatus(String.valueOf(letter.get("applicationStatus")))
                            .job(String.valueOf(letter.get("job")))
                            .build());
                }
                pageList.add(dtos);
            }

        }
        return pageList;
    }

    public static List<ResumeTitleResponseDto> resumeTitleList(Object object) {
        List<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;
        List<ResumeTitleResponseDto> dto = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {
            Integer id = (Integer) lhm.get("id");
            dto.add(ResumeTitleResponseDto.builder()
                    .id(id.longValue())
                    .title((String) lhm.get("title"))
                    .build());
        }
        return dto;
    }

    public static List<CoverLetterTitleResponseDto> coverLetterTitleList(Object object) {
        List<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;
        List<CoverLetterTitleResponseDto> dto = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {
            Integer id = (Integer) lhm.get("id");
            dto.add(CoverLetterTitleResponseDto.builder()
                    .id(id.longValue())
                    .title((String) lhm.get("title"))
                    .build());
        }
        return dto;
    }

    private static String divideTime(String time) {
        String[] ts = time.split("T");
        return ts[0] + " " + ts[1];
    }

    public static List<List<ManagePostsResponseDto>> companyList(Object object, HttpSession session) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;
        List<List<ManagePostsResponseDto>> pageList = new ArrayList<>();

        for (LinkedHashMap<String, Object> lhm : data) {
            List<ManagePostsResponseDto> dtos = new ArrayList<>();
            Integer numberOfElements = (Integer) lhm.get("numberOfElements");
            if (numberOfElements > 0) {
                ArrayList<LinkedHashMap<String, Object>> content = (ArrayList<LinkedHashMap<String, Object>>) lhm.get("content");
                for (LinkedHashMap<String, Object> dto : content) {
                    Integer id = (Integer) dto.get("id");
                    Integer applicant = 0;
                    try {
                        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/post/" + id + "/applicant/num");
                        applicant = (Integer) response.getData();
                    } catch (ClassCastException e) {
                        log.error(e.getMessage());
                    }

                    dtos.add(ManagePostsResponseDto.builder()
                            .id(id.longValue())
                            .postType((String) dto.get("postType"))
                            .closed((Boolean) dto.get("closed"))
                            .createdAt(divideTime((String) dto.get("createdAt")))
                            .title((String) dto.get("title"))
                            .endDate(divideTime((String) dto.get("endDate")))
                            .applicant(applicant)
                            .build());
                }
                pageList.add(dtos);
            }
        }
        return pageList;
    }

    public static List<StackAndJobResponseDto> stackList(Object object) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<StackAndJobResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {

            dtos.add(StackAndJobResponseDto.builder()
                    .id(lhm.get("id"))
                    .name(lhm.get("name"))
                    .build());
        }
        return dtos;
    }
}


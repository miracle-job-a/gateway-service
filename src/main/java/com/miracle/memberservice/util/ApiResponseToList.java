package com.miracle.memberservice.util;

import com.miracle.memberservice.dto.request.JobRequestDto;
import com.miracle.memberservice.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApiResponseToList {

    private final S3Method s3Method;

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

    public static List<List<TotalSearchPostResponseDto>> searchTotalPost(List<LinkedHashMap<String, Object>> data, HttpSession session) {
        List<List<TotalSearchPostResponseDto>> pageList = new ArrayList<>();

        for (LinkedHashMap<String, Object> lhm : data) {
            List<TotalSearchPostResponseDto> dtos = new ArrayList<>();
            Integer numberOfElements = (Integer) lhm.get("numberOfElements");
            if (numberOfElements > 0) {
                ArrayList<LinkedHashMap<String, Object>> content = (ArrayList<LinkedHashMap<String, Object>>) lhm.get("content");
                for (LinkedHashMap<String, Object> dto : content) {
                    Integer id = (Integer) dto.get("id");
                    List<Integer> jobs = (ArrayList<Integer>) dto.get("jobIdSet");

                    ApiResponse response;
                    try {
                        response = ServiceCall.post(session, new JobRequestDto(jobs), Const.RequestHeader.ADMIN, "/admin/jobs");
                    } catch (ClassCastException e) {
                        response = new ApiResponse(false);
                    }
                    String jobDetail;
                    if (response.getData() instanceof Boolean) {
                        jobDetail = null;
                    } else {
                        List<JobResponseDto> job = ApiResponseToList.jobs(response.getData());
                        jobDetail = job.get(0).getName();
                    }

                    dtos.add(TotalSearchPostResponseDto.builder()
                            .id(id.longValue())
                            .title((String) dto.get("title"))
                            .endDate((String) dto.get("endDate"))
                            .workAddress((String) dto.get("workAddress"))
                            .career((Integer) dto.get("career"))
                            .job(jobDetail)
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
                    .photo((String) lhm.get("photo"))
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
                    Long interviewIdValue = (interviewId == null) ? null : interviewId.longValue();

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

    public static List<List<ApplicantListResponseDto>> applicantList(Object object) {
        List<ArrayList<LinkedHashMap<String, Object>>> data = (ArrayList<ArrayList<LinkedHashMap<String, Object>>>) object;
        List<List<ApplicantListResponseDto>> pageList = new ArrayList<>();

        for (ArrayList<LinkedHashMap<String, Object>> page : data) {
            List<ApplicantListResponseDto> dtos = new ArrayList<>();
            if (!page.isEmpty()) {
                for (Map<String, Object> letter : page) {
                    Integer applicationLetterId = (Integer) letter.get("applicationLetterId");
                    Integer userId = (Integer) letter.get("userId");

                    dtos.add(ApplicantListResponseDto.builder()
                            .applicationLetterId(applicationLetterId.longValue())
                            .userId(userId.longValue()) //TODO
                            .submitDate(String.valueOf(letter.get("submitDate")))
                            .address(String.valueOf(letter.get("address")))
                            .resumeTitle(String.valueOf(letter.get("resumeTitle")))
                            .name(String.valueOf(letter.get("name")))
                            .email(String.valueOf(letter.get("email")))
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

    public static List<StackAndJobResponseDto> stackAndJobList(Object object) {
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

    public static List<CompanyNameResponseDto> companyInfo(HttpSession session, Object object) {
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<CompanyNameResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> info : data) {

            Integer postId = (Integer) info.get("postId");
            Integer companyId = (Integer) info.get("companyId");
            ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/" + postId);
            Map<String, Object> titleData = (LinkedHashMap<String, Object>) response.getData();
            String title = (String) titleData.get("title");
            dtos.add(CompanyNameResponseDto.builder()
                    .postId(postId.longValue())
                    .companyId(companyId.longValue())
                    .companyName((String) info.get("companyName"))
                    .title(title)
                    .build());
        }
        return dtos;
    }

    public static List<List<UserListResponseDto>> userList(Object object) {
        List<List<UserListResponseDto>> pageList = new ArrayList<>();

        if (object instanceof List) {
            List<?> outerList = (List<?>) object;

            for (Object innerList : outerList) {
                if (innerList instanceof List) {
                    List<?> dataList = (List<?>) innerList;

                    List<UserListResponseDto> dtos = dataList.stream()
                            .filter(dtoItem -> dtoItem instanceof Map)
                            .map(dtoItem -> (Map<?, ?>) dtoItem)
                            .map(dto -> UserListResponseDto.builder()
                                    .id(((Number) dto.get("id")).longValue())
                                    .email(dto.get("email"))
                                    .name(dto.get("name"))
                                    .address(dto.get("address"))
                                    .createdAt(dto.get("joinDate"))
                                    .build())
                            .collect(Collectors.toList());

                    pageList.add(dtos);
                }
            }
        }

        return pageList;
    }

    public static List<List<CompanyListResponseDto>> companyList(Object object) {
        List<List<CompanyListResponseDto>> pageList = new ArrayList<>();

        if (object instanceof List) {
            List<?> outerList = (List<?>) object;

            for (Object outerItem : outerList) {
                if (outerItem instanceof Map) {
                    Map<?, ?> outerMap = (Map<?, ?>) outerItem;

                    if (outerMap.containsKey("content")) {
                        Object contentObject = outerMap.get("content");
                        if (contentObject instanceof List) {
                            List<?> contentList = (List<?>) contentObject;

                            List<CompanyListResponseDto> dtos = contentList.stream()
                                    .filter(dtoItem -> dtoItem instanceof Map)
                                    .map(dtoItem -> (Map<?, ?>) dtoItem)
                                    .map(dto -> CompanyListResponseDto.builder()
                                            .id(((Number) dto.get("id")).longValue())
                                            .email(dto.get("email"))
                                            .name(dto.get("name"))
                                            .employeeNum(dto.get("employeeNum"))
                                            .bno(dto.get("bno"))
                                            .status(dto.get("status"))
                                            .approveStatus(dto.get("approveStatus"))
                                            .createdAt(dto.get("createdAt"))
                                            .build())
                                    .collect(Collectors.toList());

                            pageList.add(dtos);
                        }
                    }
                }
            }
        }
        return pageList;
    }

    public static Map<Integer, Long> getUserJoinCountByDay(List<List<LinkedHashMap<String, Object>>> data, int year, int month) {
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        Map<Integer, Long> userJoinCountByDay = new HashMap<>();

        for (int i = 1; i <= daysInMonth; i++) {
            userJoinCountByDay.put(i, 0L);
        }

        data.stream()
                .flatMap(List::stream)
                .map(user -> (String) user.get("joinDate"))
                .map(joinDate -> LocalDate.parse(joinDate).getDayOfMonth())
                .forEach(day -> userJoinCountByDay.merge(day, 1L, Long::sum));

        return userJoinCountByDay;
    }

    public static Map<Integer, Long> getCompanyJoinCountByDay(List<List<LinkedHashMap<String, Object>>> data, int year, int month) {
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        Map<Integer, Long> companyJoinCountByDay = new HashMap<>();

        for (int i = 1; i <= daysInMonth; i++) {
            companyJoinCountByDay.put(i, 0L);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        data.stream()
                .flatMap(List::stream)
                .map(company -> (String) company.get("createdAt"))
                .map(createdAt -> LocalDateTime.parse(createdAt, formatter).toLocalDate().getDayOfMonth())
                .forEach(day -> companyJoinCountByDay.merge(day, 1L, Long::sum));

        return companyJoinCountByDay;
    }

    public Map<String, List<MainPagePostsResponseDto>> mainPage(HttpSession session, Map<String, Object> data) {
        List<LinkedHashMap<String, Object>> deadlineResponse = (ArrayList<LinkedHashMap<String, Object>>) data.get("deadline");
        List<LinkedHashMap<String, Object>> newestResponse = (ArrayList<LinkedHashMap<String, Object>>) data.get("newest");

        List<MainPagePostsResponseDto> deadline = mainPageComposition(session, deadlineResponse);
        List<MainPagePostsResponseDto> newest = mainPageComposition(session, newestResponse);

        return Map.of("deadline", deadline, "newest", newest);
    }

    private List<MainPagePostsResponseDto> mainPageComposition(HttpSession session, List<LinkedHashMap<String, Object>> response) {
        List<MainPagePostsResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : response) {
            Integer id = (Integer) lhm.get("id");
            Integer companyId = (Integer) lhm.get("companyId");
            List<Integer> jobs = (ArrayList<Integer>) lhm.get("jobIdSet");
            ApiResponse job;
            try {
                job = ServiceCall.post(session, new JobRequestDto(jobs), Const.RequestHeader.ADMIN, "/admin/jobs");
            } catch (ClassCastException e) {
                job = new ApiResponse(false);
            }
            String jobDetail;
            if (job.getData() instanceof Boolean) {
                jobDetail = null;
            } else {
                List<JobResponseDto> jobNames = ApiResponseToList.jobs(job.getData());
                jobDetail = jobNames.get(0).getName();
            }
            dtos.add(MainPagePostsResponseDto.builder()
                    .id(id.longValue())
                    .companyId(companyId.longValue())
                    .postType((String) lhm.get("postType"))
                    .title((String) lhm.get("title"))
                    .photo(s3Method.getUrl(Const.RequestHeader.COMPANY, (String) lhm.get("photo")))
                    .endDate(divideTime((String) lhm.get("endDate")))
                    .workAddress((String) lhm.get("workAddress"))
                    .jobIdSet(jobDetail)
                    .career((Integer) lhm.get("career"))
                    .name((String) lhm.get("name"))
                    .build());
        }
        return dtos;
    }

    public List<List<ConditionalSearchPostResponseDto>> searchPosts(Object object, HttpSession session) {
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

                    ApiResponse response;
                    try {
                        response = ServiceCall.post(session, new JobRequestDto(jobs), Const.RequestHeader.ADMIN, "/admin/jobs");
                    } catch (ClassCastException e) {
                        response = new ApiResponse(false);
                    }
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
                            .photo(s3Method.getUrl(Const.RequestHeader.COMPANY, (String) dto.get("photo")))
                            .build());
                }
                pageList.add(dtos);
            }
        }
        return pageList;
    }
}
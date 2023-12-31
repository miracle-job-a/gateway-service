package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.*;
import com.miracle.memberservice.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

    private final S3Method s3Method;

    public PageMoveWithMessage mainPage(HttpSession session) {
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/main");
        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        ApiResponseToList apiResponseToList = new ApiResponseToList(s3Method);
        return new PageMoveWithMessage("redirect:/v1", apiResponseToList.mainPage(session, data));
    }

    public Object mainPageCompany(HttpSession session, Long companyId) {
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/count");
        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        return data;
    }

    public ResponseEntity<String> bnoCertify(CompanyCheckBnoRequestDto bno, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, bno, Const.RequestHeader.COMPANY, "/company/bno");

        return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
    }

    public PageMoveWithMessage join(CompanyJoinDto companyJoinDto, HttpSession session, MultipartFile photo) throws IOException {

        ApiResponse response = ServiceCall.post(session, companyJoinDto, Const.RequestHeader.COMPANY, "/company/signup");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("guest/company-join", response.getMessage());

        s3Method.uploadFile(photo, Const.RequestHeader.COMPANY, companyJoinDto.getBno());
        return new PageMoveWithMessage("guest/company-login");
    }

    public PageMoveWithMessage login(LoginDto loginDto, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, loginDto, loginDto.getMemberType(), "/company/login");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("guest/company-login", response.getMessage());

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        CompanyLoginResponseDto dto = CompanyLoginResponseDto.builder()
                .id(data.get("id"))
                .email(data.get("email"))
                .bno(data.get("bno"))
                .build();

        return new PageMoveWithMessage("redirect:/v1", dto);
    }

    public ResponseEntity<String> duplicateEmail(HttpSession session, String email) {

        ApiResponse response = ServiceCall.post(session, new CompanyCheckEmailRequestDto(email), Const.RequestHeader.COMPANY, "/company/email");

        return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
    }

    //공고관리 목록
    public PageMoveWithMessage postList(HttpSession session, int strNum, int endNum, String sort) {

        Long companyId = (Long) session.getAttribute("id");

        ApiResponse response = ServiceCall.getParamList(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts", strNum, endNum, sort);

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("redirect:/v1", response.getMessage());

        List<List<ManagePostsResponseDto>> postList = ApiResponseToList.postList(response.getData(), session);

        return new PageMoveWithMessage("company/post-list", postList);
    }

    public PageMoveWithMessage postList(HttpSession session, Long companyId, int strNum, int endNum, String sort) {
        ApiResponse response = ServiceCall.getParamList(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts", strNum, endNum, sort);
        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("redirect:/v1", response.getMessage());

        List<List<ManagePostsResponseDto>> postList = ApiResponseToList.postList(response.getData(), session);

        return new PageMoveWithMessage("admin/post-popular", postList);
    }

    // 확인 용도
    public Boolean statusCompany(HttpSession session) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse apiResponse = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/status");
        return (Boolean) apiResponse.getData();
    }

    public PageMoveWithMessage formPost(HttpSession session, String postType, Long companyId) {
        if (Objects.isNull(companyId)) companyId = (Long) session.getAttribute("id");

        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/info");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/company/post/list/1", response.getMessage());

        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        List<CompanyFaqResponseDto> dtos = ApiResponseToList.faqList(data.get("faqList"));

        PostCommonDataResponseDto info = PostCommonDataResponseDto.builder()
                .name(data.get("name"))
                .ceoName(data.get("ceoName"))
                .photo(s3Method.getUrl(Const.RequestHeader.COMPANY, (String) data.get("photo")))
                .employeeNum(data.get("employeeNum"))
                .address(data.get("address"))
                .introduction(data.get("introduction"))
                .faqList(dtos)
                .build();

        if (!Objects.isNull(postType)) {
            return new PageMoveWithMessage("company/mz-post", info);
        }
        return new PageMoveWithMessage("company/normal-post", info);
    }

    public PageMoveWithMessage createPost(HttpSession session, PostCreateRequestDto postRequestDto) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, postRequestDto, Const.RequestHeader.COMPANY, "/company/" + companyId + "/post");

        return new PageMoveWithMessage("redirect:/v1/company/post/list/1", response.getMessage());
    }

    public PageMoveWithMessage getPostDetail(HttpSession session, Long postId, String postType, Long companyId) {
        if (Objects.isNull(companyId)) companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/" + postId);

        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        PostResponseDto dto;

        PostResponseDto.PostResponseDtoBuilder builder = PostResponseDto.builder()
                .career((Integer) data.get("career"))
                .postType(postType)
                .benefit((String) data.get("benefit"))
                .process((String) data.get("process"))
                .notice((String) data.get("notice"))
                .mainTask((String) data.get("mainTask"))
                .qualification((String) data.get("qualification"))
                .title((String) data.get("title"))
                .specialSkill((String) data.get("specialSkill"))
                .tool((String) data.get("tool"))
                .workCondition((String) data.get("workCondition"))
                .workAddress((String) data.get("workAddress"))
                .endDate(truncatedTo(data.get("endDate")))
                .questionList(ApiResponseToList.questionList(data.get("questionList")))
                .stackIdSet((ArrayList<Integer>) data.get("stackIdSet"))
                .jobIdSet((ArrayList<Integer>) data.get("jobIdSet"))
                .closed((Boolean) data.get("closed"));

        if (postType.equals("NORMAL")) {
            dto = builder.build();
        } else {
            dto = builder
                    .testStartDate(truncatedTo(data.get("testStartDate")))
                    .testEndDate(truncatedTo(data.get("testEndDate")))
                    .build();
            return new PageMoveWithMessage("company/mzPost-detail", dto);
        }

        return new PageMoveWithMessage("company/post-detail", dto);
    }

    public PageMoveWithMessage deletePost(HttpSession session, Long postId) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/" + postId);

        return new PageMoveWithMessage("redirect:/v1/company/post/list/1", response.getMessage());
    }

    public PageMoveWithMessage closePost(HttpSession session, Long postId, String postType) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/" + postId + "/close");

        return new PageMoveWithMessage("redirect:/v1/company/post/detail?id=" + postId + "&postType=" + postType, response.getMessage());
    }

    public PageMoveWithMessage updatePost(HttpSession session, PostRequestDto postEditRequestDto, IdEditDto idList, QuestionEditDto questionList) {
        Long companyId = (Long) session.getAttribute("id");
        Long postId = postEditRequestDto.getPostId();

        if (!Objects.isNull(idList.getIdList()))
            postEditRequestDto.addAllQuestion(questionRequestDtos(idList, questionList));

        ApiResponse response = ServiceCall.put(session, postEditRequestDto, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/" + postId);

        return new PageMoveWithMessage("redirect:/v1/company/post/detail?id=" + postId + "&postType=" + postEditRequestDto.getPostType(), response.getMessage());
    }

    public PageMoveWithMessage faqList(HttpSession session) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/faqs");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1", response.getMessage());

        List<CompanyFaqResponseDto> dtos = ApiResponseToList.faqList(response.getData());

        return new PageMoveWithMessage("company/faq", dtos);
    }

    public PageMoveWithMessage addFaq(HttpSession session, CompanyFaqRequestDto companyFaqRequestDto) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, companyFaqRequestDto, Const.RequestHeader.COMPANY, "/company/" + companyId + "/faq");
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/company/faq", response.getMessage());
        return new PageMoveWithMessage("redirect:/v1/company/faq");
    }

    public PageMoveWithMessage deleteFaq(HttpSession session, String faqId) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/faqs/" + faqId);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/company/faq", response.getMessage());
        return new PageMoveWithMessage("redirect:/v1/company/faq");
    }

    public PageMoveWithMessage searchPosts(HttpSession session, ConditionalSearchPostRequestDto dto, int strNum, int endNum) {

        ApiResponse response = ServiceCall.postParam(session, dto, Const.RequestHeader.COMPANY, "/company/posts/search", strNum, endNum);
        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("redirect:/v1", response.getMessage());

        ApiResponseToList apiResponseToList = new ApiResponseToList(s3Method);
        List<List<ConditionalSearchPostResponseDto>> searchPosts = apiResponseToList.searchPosts(response.getData(), session);

        return new PageMoveWithMessage("guest/search-post", searchPosts);
    }

    public PageMoveWithMessage searchTotalPosts(HttpSession session, String search, int strNum, int endNum) {

        ApiResponse response = ServiceCall.getParamSearch(session, Const.RequestHeader.COMPANY, "/company", strNum, endNum, search);
        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("redirect:/v1", response.getMessage());

        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        List<LinkedHashMap<String, Object>> postList = (ArrayList<LinkedHashMap<String, Object>>) data.get("postList");

        List<List<TotalSearchPostResponseDto>> postPage = ApiResponseToList.searchTotalPost(postList, session);

        return new PageMoveWithMessage("guest/total-search", Map.of("postPage", postPage));
    }

    private LocalDateTime truncatedTo(Object time) {
        LocalDateTime localDateTime = LocalDateTime.parse((String) time);
        return localDateTime.truncatedTo(ChronoUnit.MINUTES);
    }

    private List<QuestionRequestDto> questionRequestDtos(IdEditDto idEditDto, QuestionEditDto questionEditDto) {
        List<Integer> idList = idEditDto.getIdList();
        List<String> questionList = questionEditDto.getQuestionList();
        List<QuestionRequestDto> questionRequestDtos = new ArrayList<>();
        int size = idList.size();
        for (int i = 0; i < size; i++) {
            QuestionRequestDto build = QuestionRequestDto.builder()
                    .id(idList.get(i).longValue())
                    .question(questionList.get(i))
                    .build();
            questionRequestDtos.add(build);
        }
        return questionRequestDtos;
    }

    //오늘 회원가입한 기업 회원 목록
    public PageMoveWithMessage getCompanyListToday(HttpSession session, int strNum, int endNum, boolean today) {

        ApiResponse response = ServiceCall.getParamListWithToday(session, Const.RequestHeader.COMPANY, "/company/list", strNum, endNum, today);
        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("error/500", response.getMessage());

        List<List<ManagePostsResponseDto>> postList = ApiResponseToList.postList(response.getData(), session);

        return new PageMoveWithMessage("admin/main", postList);
    }

    public PageMoveWithMessage getCompanyInfo(HttpSession session) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId);

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("error/500", response.getMessage());

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        String photo = (String) data.get("photo");
        CompanyPageResponseDto info = CompanyPageResponseDto.builder()
                .companyId((Integer) data.get("companyId"))
                .approveStatus((Boolean) data.get("approveStatus"))
                .name((String) data.get("name"))
                .ceoName((String) data.get("ceoName"))
                .photo(photo)
                .employeeNum((Integer) data.get("employeeNum"))
                .address((String) data.get("address"))
                .introduction((String) data.get("introduction"))
                .sector((String) data.get("sector"))
                .bnoStatus((Boolean) data.get("bnoStatus"))
                .countOpen((Integer) data.get("countOpen"))
                .bno(photo)
                .build();

        return new PageMoveWithMessage("company/company-info", info);

    }

    public Boolean checkCompanyInfo(HttpSession session, CompanyLoginRequestDto requestDto) {
        requestDto.setEmail((String) session.getAttribute("email"));
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, requestDto, Const.RequestHeader.COMPANY, "/company/" + companyId);
        if (response.getHttpStatus() != 200) {
            return false;
        }

        Boolean status = (Boolean) response.getData();
        return status;
    }

    public PageMoveWithMessage modifyCompanyInfo(HttpSession session) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId);

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("error/500", response.getMessage());

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        String photo = (String) data.get("photo");
        CompanyPageResponseDto info = CompanyPageResponseDto.builder()
                .companyId((Integer) data.get("companyId"))
                .approveStatus((Boolean) data.get("approveStatus"))
                .name((String) data.get("name"))
                .ceoName((String) data.get("ceoName"))
                .photo(photo)
                .employeeNum((Integer) data.get("employeeNum"))
                .address((String) data.get("address"))
                .introduction((String) data.get("introduction"))
                .sector((String) data.get("sector"))
                .bnoStatus((Boolean) data.get("bnoStatus"))
                .countOpen((Integer) data.get("countOpen"))
                .bno(photo)
                .build();

        return new PageMoveWithMessage("company/modify-form", info);
    }

    public PageMoveWithMessage updateCompanyInfo(HttpSession session, CompanyInfoRequestDto requestDto, MultipartFile photo) throws IOException {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.put(session, requestDto, Const.RequestHeader.COMPANY, "/company/" + companyId);

        if (response.getHttpStatus() != 200) {
            return new PageMoveWithMessage("redirect:/v1/company/info", response.getMessage());
        }

        String bno = requestDto.getBno();
        String originalFilename = photo.getOriginalFilename();
        if (!Strings.isBlank(originalFilename)) {
            s3Method.deleteFile(Const.RequestHeader.COMPANY, bno);
            s3Method.uploadFile(photo, Const.RequestHeader.COMPANY, bno);
        }

        return new PageMoveWithMessage("redirect:/v1/company/info");
    }

    public PageMoveWithMessage approveCompany(HttpSession session, String companyId) {
        ApiResponse response = ServiceCall.putApproveCompany(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/approval");
        if (response.getHttpStatus() != 200) {
            return new PageMoveWithMessage("admin/main", response.getMessage());
        } else if (response.getData() instanceof Boolean && (Boolean) response.getData() == false) {
            return new PageMoveWithMessage("admin/company-list", response.getMessage());
        } else {
            return new PageMoveWithMessage("redirect:/v1/admin/company/list/1/5");
        }
    }

    public PageMoveWithMessage getTodayPostCount(int year, int month, HttpSession session) {
        ApiResponse response = ServiceCall.getParams(session, Const.RequestHeader.COMPANY, "/company/posts/today", year, month);
        if (response.getHttpStatus() != 200) {
            return new PageMoveWithMessage("admin/main", response.getMessage());
        } else {
            List<Map<String, Object>> postData = (List<Map<String, Object>>) response.getData();
            Map<Integer, List<Integer>> dayCounts = new HashMap<>();

            for (Map<String, Object> post : postData) {
                LocalDateTime createdAt = LocalDateTime.parse((CharSequence) post.get("createdAt"));
                int dayOfMonth = createdAt.getDayOfMonth();

                String postType = (String) post.get("postType");

                dayCounts.putIfAbsent(dayOfMonth, new ArrayList<>(Arrays.asList(0, 0)));

                List<Integer> countList = dayCounts.get(dayOfMonth);
                if ("NORMAL".equals(postType)) {
                    countList.set(1, countList.get(1) + 1);
                } else if ("MZ".equals(postType)) {
                    countList.set(0, countList.get(0) + 1);
                }
            }

            List<List<Integer>> formattedData = new ArrayList<>();
            for (int i = 1; i <= 31; i++) {
                List<Integer> countList = dayCounts.getOrDefault(i, Arrays.asList(0, 0));
                formattedData.add(Arrays.asList(i, countList.get(0), countList.get(1)));
            }
            return new PageMoveWithMessage("admin/today-post-chart", formattedData);
        }
    }

    public PageMoveWithMessage signoutCompany(HttpSession session) {
        Long companyId = (Long) session.getAttribute("id");
        String bno = (String) session.getAttribute("bno");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.COMPANY, "/company/" + companyId);
        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("redirect:/v1/company/info");

        s3Method.deleteFile(Const.RequestHeader.COMPANY, bno);
        session.invalidate();
        return new PageMoveWithMessage("redirect:/v1/company/logout");
    }

    public Object getStackChartData(HttpSession session, Long companyId) {
        ApiResponse adminResponse = ServiceCall.get(session, Const.RequestHeader.ADMIN, "/admin/stacks");
        if (adminResponse.getHttpStatus() != 200)
            return null;

        ApiResponse companyResponse = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/jobstacks");
        if (companyResponse.getHttpStatus() != 200)
            return null;

        List<Map<String, Object>> adminDataList = (List<Map<String, Object>>) adminResponse.getData();
        List<Map<String, Object>> companyDataList = (List<Map<String, Object>>) companyResponse.getData();

        Map<Long, String> stackIdToNameMap = new HashMap<>();
        Map<String, Integer> stackNameToCountMap = new HashMap<>();

        for (Map<String, Object> adminData : adminDataList) {
            Number stackId = (Number) adminData.get("id");
            String stackName = (String) adminData.get("name");
            stackIdToNameMap.put(stackId.longValue(), stackName);
        }

        for (Map<String, Object> companyData : companyDataList) {
            List<Number> stackIdSet = (List<Number>) companyData.get("stackIdSet");

            for (Number stackId : stackIdSet) {
                String stackName = stackIdToNameMap.get(stackId.longValue());

                stackNameToCountMap.put(stackName, stackNameToCountMap.getOrDefault(stackName, 0) + 1);
            }
        }

        List<List<Object>> result = new ArrayList<>();
        result.add(List.of("Stack", "num"));

        for (Map.Entry<String, Integer> entry : stackNameToCountMap.entrySet()) {
            String stackName = entry.getKey();
            Integer count = entry.getValue();
            result.add(List.of(stackName, count));
        }

        return result;
    }

    public Object getJobChartData(HttpSession session, Long companyId) {
        ApiResponse adminResponse = ServiceCall.get(session, Const.RequestHeader.ADMIN, "/admin/jobs");
        if (adminResponse.getHttpStatus() != 200)
            return null;

        ApiResponse companyResponse = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/jobstacks");
        if (companyResponse.getHttpStatus() != 200)
            return null;

        List<Map<String, Object>> adminDataList = (List<Map<String, Object>>) adminResponse.getData();
        List<Map<String, Object>> companyDataList = (List<Map<String, Object>>) companyResponse.getData();

        Map<Long, String> jobIdToNameMap = new HashMap<>();
        Map<String, Integer> jobNameToCountMap = new HashMap<>();

        for (Map<String, Object> adminData : adminDataList) {
            Number jobId = (Number) adminData.get("id");
            String jobName = (String) adminData.get("name");
            jobIdToNameMap.put(jobId.longValue(), jobName);
        }

        for (Map<String, Object> companyData : companyDataList) {
            List<Number> jobIdSet = (List<Number>) companyData.get("jobIdSet");

            for (Number jobId : jobIdSet) {
                String jobName = jobIdToNameMap.get(jobId.longValue());

                jobNameToCountMap.put(jobName, jobNameToCountMap.getOrDefault(jobName, 0) + 1);
            }
        }

        List<List<Object>> result = new ArrayList<>();
        result.add(List.of("Job", "num"));

        for (Map.Entry<String, Integer> entry : jobNameToCountMap.entrySet()) {
            String jobName = entry.getKey();
            Integer count = entry.getValue();
            result.add(List.of(jobName, count));
        }

        return result;
    }

    public PageMoveWithMessage getCompanyJoinCountByDay(HttpSession session, int year, int month) {
        ApiResponse response = ServiceCall.getParamListWithTodayFalse(session, Const.RequestHeader.COMPANY, "/company/list", 1, 1);
        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("admin/main", response.getMessage());

        List<Map<String, Object>> companyData = (List<Map<String, Object>>) response.getData();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        List<LinkedHashMap<String, Object>> filterCompanyData = companyData.stream()
                .flatMap(map -> ((List<LinkedHashMap<String, Object>>) map.get("content")).stream())
                .filter(company -> {
                    String createdAt = (String) company.get("createdAt");
                    LocalDateTime companyJoinDateTime = LocalDateTime.parse(createdAt, formatter);
                    LocalDate companyJoinDate = companyJoinDateTime.toLocalDate();
                    return companyJoinDate.getYear() == year && companyJoinDate.getMonthValue() == month;
                })
                .collect(Collectors.toList());

        Map<Integer, Long> companyJoinCountByDay = ApiResponseToList.getCompanyJoinCountByDay(Collections.singletonList(filterCompanyData), year, month);

        List<List<Object>> chartData = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : companyJoinCountByDay.entrySet()) {
            chartData.add(Arrays.asList(entry.getKey(), entry.getValue()));
        }

        return new PageMoveWithMessage("admin/company-join-count", companyJoinCountByDay);
    }
}

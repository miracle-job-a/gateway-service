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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
            return new PageMoveWithMessage("admin/companyList", response.getMessage());
        } else {
            return new PageMoveWithMessage("redirect:/v1/admin/company/list/1/5");
        }
    }

    public PageMoveWithMessage getPostCount(HttpSession session) {
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/posts/count");
        if (response.getHttpStatus() != 200) {
            return new PageMoveWithMessage("admin/main", response.getMessage());
        } else {
            Object responseData = response.getData();

            if (responseData instanceof Map) {
                Map<String, Integer> postCounts = (Map<String, Integer>) responseData;

                List<List<Object>> chartData = new ArrayList<>();
                chartData.add(Arrays.asList("Type", "Num"));

                for (Map.Entry<String, Integer> entry : postCounts.entrySet()) {
                    String type;
                    switch (entry.getKey()) {
                        case "countNormalPosts":
                            type = "일반 공고";
                            break;
                        case "countMZPosts":
                            type = "MZ 공고";
                            break;
                        default:
                            type = entry.getKey();
                    }

                    chartData.add(Arrays.asList(type, entry.getValue()));
                }

                return new PageMoveWithMessage("admin/postChart", chartData);
            } else {
                return new PageMoveWithMessage("admin/main", response.getMessage());
            }
        }
    }

    public PageMoveWithMessage getTodayPostCount(int year, HttpSession session) {
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/posts/" + year + "/today");

        if (response.getHttpStatus() != 200) {
            return new PageMoveWithMessage("admin/main", response.getMessage());
        } else {
            List<Map<String, Object>> postData = (List<Map<String, Object>>) response.getData();

            Map<Integer, Integer> monthCounts = new HashMap<>();

            for (Map<String, Object> post : postData) {
                LocalDateTime createdAt = LocalDateTime.parse((CharSequence) post.get("createdAt"));
                int month = createdAt.getMonthValue();
                monthCounts.put(month, monthCounts.getOrDefault(month, 0) + 1);
            }

            List<List<Integer>> formattedData = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                formattedData.add(List.of(i, monthCounts.getOrDefault(i, 0)));
            }
            return new PageMoveWithMessage("admin/todayPostChart", formattedData);
        }
    }
}

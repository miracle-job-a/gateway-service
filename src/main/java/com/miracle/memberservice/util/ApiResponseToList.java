package com.miracle.memberservice.util;

import com.miracle.memberservice.dto.response.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ApiResponseToList {
    public static List<CompanyFaqResponseDto> faqList(Object object){
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

    public static List<StackResponseDto> stacks(Object object){
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

    public static List<JobResponseDto> jobs(Object object){
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

    public static List<ManagePostsResponseDto> postList(Object object){
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<ManagePostsResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {

            Integer id = (Integer) lhm.get("id");
            dtos.add(ManagePostsResponseDto.builder()
                    .id(id.longValue())
                    .postType((String) lhm.get("postType"))
                    .closed((Boolean)lhm.get("closed"))
                    .createdAt((String)lhm.get("createdAt"))
                    .title((String)lhm.get("title"))
                    .endDate((String)lhm.get("endDate"))
                    .build());
        }
        return dtos;
    }

    public static List<QuestionResponseDto> questionList(Object object){
        ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) object;

        List<QuestionResponseDto> dtos = new ArrayList<>();
        for (LinkedHashMap<String, Object> lhm : data) {

            Integer id = (Integer) lhm.get("id");
            dtos.add(QuestionResponseDto.builder()
                    .id(id.longValue())
                    .question((String)lhm.get("question"))
                    .build());
        }
        return dtos;
    }
}

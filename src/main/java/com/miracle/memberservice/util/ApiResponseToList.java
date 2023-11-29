package com.miracle.memberservice.util;

import com.miracle.memberservice.dto.response.CompanyFaqResponseDto;
import com.miracle.memberservice.dto.response.JobResponseDto;
import com.miracle.memberservice.dto.response.StackResponseDto;

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
}

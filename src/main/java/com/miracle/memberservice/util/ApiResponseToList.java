package com.miracle.memberservice.util;

import com.miracle.memberservice.dto.response.CompanyFaqResponseDto;
import com.miracle.memberservice.dto.response.JobResponseDto;
import com.miracle.memberservice.dto.response.StackResponseDto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ApiResponseToList {
    public static List<CompanyFaqResponseDto> faqList(Object object){
        ArrayList data = (ArrayList) object;
        int size = data.size();

        List<CompanyFaqResponseDto> dtos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) data.get(i);

            dtos.add(CompanyFaqResponseDto.builder()
                    .id(map.get("id"))
                    .question(map.get("question"))
                    .answer(map.get("answer"))
                    .build());
        }
        return dtos;
    }

    public static List<StackResponseDto> stackList(Object object){
        ArrayList data = (ArrayList) object;
        int size = data.size();

        List<StackResponseDto> dtos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) data.get(i);

            dtos.add(StackResponseDto.builder()
                    .id(map.get("id"))
                    .name(map.get("name"))
                    .build());
        }
        return dtos;
    }

    public static List<JobResponseDto> jobList(Object object){
        ArrayList data = (ArrayList) object;
        int size = data.size();

        List<JobResponseDto> dtos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) data.get(i);

            dtos.add(JobResponseDto.builder()
                    .id(map.get("id"))
                    .name(map.get("name"))
                    .build());
        }
        return dtos;
    }
}

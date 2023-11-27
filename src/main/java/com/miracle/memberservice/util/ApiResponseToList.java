package com.miracle.memberservice.util;

import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.CompanyFaqResponseDto;

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
}

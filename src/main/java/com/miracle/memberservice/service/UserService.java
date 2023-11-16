package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.dto.response.ServiceApiResponse;
import com.miracle.memberservice.util.PageMoveWithMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class UserService {
    public PageMoveWithMessage userJoin(UserJoinDto userJoinDto) {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:60001")
                .path("/v1/user/join")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ServiceApiResponse> serverApiResponse = restTemplate.postForEntity(uri, userJoinDto, ServiceApiResponse.class);
        ServiceApiResponse<UserJoinDto> apiResponse = serverApiResponse.getBody();
        return joinCheck(apiResponse);
    }

    private PageMoveWithMessage joinCheck(ServiceApiResponse<UserJoinDto> apiResponse) {
        int httpStatus = apiResponse.getHttpStatus();
        if (httpStatus == 200) {
            return new PageMoveWithMessage("index", apiResponse.getMessage());
        }
        return new PageMoveWithMessage("guest/user-join", apiResponse.getMessage());
    }

}

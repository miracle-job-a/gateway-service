package com.miracle.memberservice.jwt.controller;

import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.jwt.domain.AccessToken;
import com.miracle.memberservice.jwt.dto.TokenRequestDto;
import com.miracle.memberservice.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/jwt")
@RestController
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<AccessToken> createToken(@RequestBody TokenRequestDto dto) {
        Long id = dto.getId();
        String memberType = dto.getMemberType();
        String email = dto.getEmail();

        AccessToken token = jwtService.createToken(id, memberType, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody TokenRequestDto dto) {
        Long id = dto.getId();
        String memberType = dto.getMemberType();
        String email = dto.getEmail();
        String token = dto.getToken();

        Boolean valid = jwtService.validateToken(id, memberType, email, token);
        return ResponseEntity.ok(valid);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse nullPointer(NullPointerException e) {
        int httpStatus = HttpStatus.BAD_REQUEST.value();
        String message = e.getMessage();
        String code = "400";
        String exception = e.getClass().getSimpleName();

        return new ApiResponse(httpStatus, message, code, exception, null);
    }
}

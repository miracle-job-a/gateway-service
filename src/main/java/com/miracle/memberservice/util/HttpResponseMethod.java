package com.miracle.memberservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@Slf4j
public class HttpResponseMethod {
    public static String makeHttpStatus(ResponseEntity<String> response){
        log.info(response.toString());

        String body = response.getBody();

        return Objects.requireNonNull(body).split(",")[0].split(":")[1];
    }

    public static String makeErrorMessage(ResponseEntity<String> response){
        String body = response.getBody();

        return body.substring(body.indexOf(":") + 2, body.lastIndexOf("\""));
    }
}

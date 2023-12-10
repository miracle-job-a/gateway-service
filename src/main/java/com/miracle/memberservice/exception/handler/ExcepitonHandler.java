package com.miracle.memberservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExcepitonHandler {
    @ExceptionHandler(value = ClassCastException.class)
    public String connectFail(Model model){
        model.addAttribute("errorMessage", "해당 서비스가 작동하지 않습니다");
        return "error/500";
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public String notSupportedPostOrGet(Model model){
        model.addAttribute("errorMessage", "비정상적인 접근입니다.");
        return "error/500";
    }
}

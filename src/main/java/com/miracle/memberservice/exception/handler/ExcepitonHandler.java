package com.miracle.memberservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

@ControllerAdvice
@Slf4j
public class ExcepitonHandler {
    @ExceptionHandler(value = ConnectException.class)
    public String connectFail(ConnectException e, Model model){
        model.addAttribute("errorMessage", e.getMessage());
        return "error/500";
    }

}

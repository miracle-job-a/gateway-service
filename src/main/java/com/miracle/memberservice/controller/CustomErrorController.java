package com.miracle.memberservice.controller;

import org.apache.http.HttpStatus;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    private final String VIEW_PATH = "error/";

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                model.addAttribute("errorMessage", "로그인이 필요합니다.");
                return "redirect:/v1/user/login-form";
            }

            if (statusCode == HttpStatus.SC_NOT_FOUND) {
                model.addAttribute("errorMessage", "해당 url이 존재하지 않습니다.");
                return VIEW_PATH + "404";
            }

            if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                model.addAttribute("errorMessage", "해당 url이 존재하지 않습니다.");
                return VIEW_PATH + "500";
            }
        }
        return null;
    }
}

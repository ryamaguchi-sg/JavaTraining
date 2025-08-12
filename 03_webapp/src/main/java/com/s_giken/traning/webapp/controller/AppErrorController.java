package com.s_giken.traning.webapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {
    @RequestMapping
    public ModelAndView error(HttpServletRequest req, ModelAndView mav) {
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }
}

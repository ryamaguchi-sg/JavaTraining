package com.s_giken.traning.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/")
public class RootController {
	@GetMapping("/")
	public String hello() {
		return "top";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
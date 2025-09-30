package com.s_giken.training.webapp.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.s_giken.training.webapp.model.entity.Account;
import com.s_giken.training.webapp.service.UserService;

@Controller
@RequestMapping("/login")
public class SignupController {

	private final UserService userService;

	public SignupController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/signup")
	public String showSignupForm(Model model) {
		model.addAttribute("account", new Account());
		return "signup"; // signup.html を表示
	}

	@PostMapping("/signup")
	public String processSignup(@Valid @ModelAttribute Account account,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "signup";
		}
		userService.add(account);

		redirectAttributes.addFlashAttribute("message", "登録が完了しました");
		return "redirect:/login/signup";
	}

}

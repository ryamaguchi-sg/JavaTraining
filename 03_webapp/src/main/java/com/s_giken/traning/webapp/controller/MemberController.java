package com.s_giken.traning.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.s_giken.traning.webapp.exception.NotFoundException;
import com.s_giken.traning.webapp.model.Member;
import com.s_giken.traning.webapp.model.MemberSearchCondition;
import com.s_giken.traning.webapp.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;

	@GetMapping("/search")
	public String showSearchCondition(Model model) {
		var memberSearchCondition = new MemberSearchCondition();
		model.addAttribute("memberSearchCondition", memberSearchCondition);
		return "member_search_condition";
	}

	@PostMapping("/search")
	public String searchAndListing(
			@ModelAttribute("memberSearchCondition") MemberSearchCondition memberSearchCodition,
			Model model) {
		var result = memberService.findAll();
		model.addAttribute("result", result);
		return "member_search_result";
	}

	@GetMapping("/edit/{id}")
	public String editMember(@PathVariable int id, Model model) {
		var member = memberService.findById(id);
		if (!member.isPresent()) {
			throw new NotFoundException("");
		}
		model.addAttribute("member", member);
		return "member_edit";
	}

	@GetMapping("/add")
	public String addMember(Model model) {
		var member = new Member();
		model.addAttribute("member", member);
		return "member_edit";
	}

	@PostMapping("/save")
	public String saveMember(@Validated Member member, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "member_edit";
		}
		memberService.save(member);
		redirectAttributes.addFlashAttribute("message", "保存しました。");
		return "redirect:/member/edit/" + member.getMemberId();
	}

	@GetMapping("/delete/{id}")
	public String deleteMember(@PathVariable int id, RedirectAttributes redirectAttributes) {
		memberService.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "削除しました。");
		return "redirect:/member/search";
	}
}

package com.s_giken.traning.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.s_giken.traning.webapp.exception.NotFoundException;
import com.s_giken.traning.webapp.model.Charge;
import com.s_giken.traning.webapp.model.ChargeSearchCondition;
import com.s_giken.traning.webapp.service.ChargeService;

@Controller
@RequestMapping("/charge")
public class ChargeController {
	@Autowired
	private ChargeService chargeService;

	@GetMapping("/search")
	public String showSearchCondition(Model model) {
		var chargeSearchCondition = new ChargeSearchCondition();
		model.addAttribute("chargeSearchCondition", chargeSearchCondition);
		return "charge_search_condition";
	}

	@PostMapping("/search")
	public String searchAndListing(
			@ModelAttribute("chargeSearchCondition") ChargeSearchCondition chargeSearchCodition,
			Model model) {
		var result = chargeService.findByCondition(chargeSearchCodition);
		model.addAttribute("result", result);
		return "charge_search_result";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, @ModelAttribute("message") String message,
			Model model) {
		var charge = chargeService.findById(id);
		if (!charge.isPresent()) {
			throw new NotFoundException("chargeId is not found.");
		}
		model.addAttribute("message", message);
		model.addAttribute("charge", charge);
		return "charge_edit";
	}

	@GetMapping("/add")
	public String add(Model model) {
		var charge = new Charge();
		model.addAttribute("charge", charge);
		return "charge_edit";
	}

	@PostMapping("/save")
	public String save(@Validated Charge charge, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "charge_edit";
		}
		chargeService.save(charge);
		redirectAttributes.addFlashAttribute("message", "保存しました");
		return "redirect:/charge/edit/" + charge.getChargeId();
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
		if (!chargeService.findById(id).isPresent()) {
			throw new NotFoundException("");
		}
		chargeService.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "削除しました");
		return "redirect:/charge/search";
	}
}

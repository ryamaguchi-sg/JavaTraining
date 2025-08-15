package com.s_giken.training.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.s_giken.training.webapp.controller.editor.PaymentMethodEditorSupport;
import com.s_giken.training.webapp.exception.NotFoundException;
import com.s_giken.training.webapp.model.PaymentMethod;
import com.s_giken.training.webapp.model.entity.Charge;
import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;
import com.s_giken.training.webapp.service.ChargeService;

/**
 * 料金管理機能のコントローラークラス
 */
@Controller // コントローラークラスであることを示す
@RequestMapping("/charge") // リクエストパスを指定
public class ChargeController {
	private final ChargeService chargeService;

	/**
	 * 料金管理機能のコントローラークラスのコンストラクタ
	 * 
	 * @param memberService 料金管理機能のサービスクラス(SpringのDIコンテナから渡される)
	 */
	public ChargeController(ChargeService chargeService) {
		this.chargeService = chargeService;
	}

	/**
	 * コントローラで受けっとったリクエストの型変換方法をカスタマイズする。
	 * 
	 * 主に、独自で定義した型を利用している場合、デフォルトの方法では対応できないときに利用する。
	 * 
	 * @param binder リクエストパラメータ
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// PaymentMethod列挙型
		// リクエスト → PaymentMethod : Paymentmethod.fromCodeメソッドを利用して PaymentMethod列挙型へ変換
		// Paymentmethod → リクエスト : Paymentmethod.getCodeメソッドを利用して、数値の文字列へ変換
		binder.registerCustomEditor(PaymentMethod.class, new PaymentMethodEditorSupport());
	}

	/**
	 * 料金検索条件画面を表示する
	 * 
	 * @param model Thymeleafに渡すデータ
	 * @return 料金検索条件画面のテンプレート名
	 */
	@GetMapping("/search")
	public String showSearchCondition(Model model) {
		var condition = new ChargeSearchCondition();
		model.addAttribute("chargeSearchCondition", condition);
		return "charge_search_condition";
	}

	/**
	 * 料金検索結果画面を表示する
	 * 
	 * @param chargeSearchCodition 料金検索条件画面で入力された検索条件
	 * @param model                Thymeleafに渡すデータ
	 * @return 料金検索結果画面のテンプレート名
	 */
	@PostMapping("/search")
	public String searchAndListing(
			@ModelAttribute("chargeSearchCondition") ChargeSearchCondition chargeSearchCodition,
			Model model) {
		var result = chargeService.findByConditions(chargeSearchCodition);
		model.addAttribute("result", result);
		return "charge_search_result";
	}

	/**
	 * 料金編集画面を表示する
	 * 
	 * @param id    URLに指定された料金ID
	 * @param model Thymeleafに渡すデータ
	 * @return 料金編集画面のテンプレート名
	 */
	@GetMapping("/edit/{id}")
	public String editCharge(
			@PathVariable Long id,
			Model model) {
		var charge = chargeService.findById(id);
		if (!charge.isPresent()) {
			throw new NotFoundException(String.format("指定したmemberId(%d)の料金情報が存在しません。", id));
		}
		model.addAttribute("isAddMode", false);
		model.addAttribute("charge", charge.get());
		return "charge_edit";
	}

	/**
	 * 料金追加画面を表示する
	 * 
	 * @param model Thymeleafに渡すデータ
	 * @return  料金追加画面のテンプレート名
	 */
	@GetMapping("/add")
	public String formAddCharge(Model model) {
		var charge = new Charge();
		model.addAttribute("isAddMode", true);
		model.addAttribute("charge", charge);
		return "charge_edit";
	}

	/**
	 * 料金情報を登録する
	 * 
	 * @param charge              料金編集画面で入力された 料金情報
	 * @param bindingResult      入力チェック結果
	 * @param redirectAttributes リダイレクト先の画面に渡すデータ
	 * @return リダイレクト先のURL
	 */
	@PostMapping("/add")
	@Transactional
	public String addCharge(
			@Validated Charge charge,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "charge_edit";
		}
		chargeService.add(charge);
		redirectAttributes.addFlashAttribute("message", "保存しました。");
		return "redirect:/charge/edit/" + charge.getChargeId();
	}

	/**
	 *  料金情報を更新する
	 * 
	 * @param charge              料金編集画面で入力された 料金情報
	 * @param bindingResult      入力チェック結果
	 * @param redirectAttributes リダイレクト先の画面に渡すデータ
	 * @return リダイレクト先のURL
	 */
	@PostMapping("/update")
	@Transactional
	public String saveCharge(
			@Validated Charge charge,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "charge_edit";
		}
		chargeService.update(charge);
		redirectAttributes.addFlashAttribute("message", "保存しました。");
		return "redirect:/charge/edit/" + charge.getChargeId();
	}

	/**
	 *  料金情報を削除する
	 * 
	 * @param id                 URLに指定された料金ID
	 * @param redirectAttributes リダイレクト先の画面に渡すデータ
	 * @return リダイレクト先のURL
	 */
	@GetMapping("/delete/{id}")
	@Transactional
	public String deleteCharge(
			@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		var charge = chargeService.findById(id);
		if (!charge.isPresent()) {
			throw new NotFoundException(String.format("指定したchargeId(%d)の料金情報が存在しません。", id));
		}

		chargeService.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "削除しました。");
		return "redirect:/charge/search";
	}
}

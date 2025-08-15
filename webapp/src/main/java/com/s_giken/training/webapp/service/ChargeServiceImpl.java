package com.s_giken.training.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.s_giken.training.webapp.exception.AttributeErrorException;
import com.s_giken.training.webapp.model.entity.Charge;
import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;
import com.s_giken.training.webapp.repository.ChargeRepository;

/**
 * 料金管理機能のサービスクラス(実態クラス)
 */
@Service
public class ChargeServiceImpl implements ChargeService {
	private ChargeRepository chargeRepository;

	/**
	 * 料金管理機能のサービスクラスのコンストラクタ
	 * 
	 * @param memberRepository 料金管理機能のリポジトリクラス(SpringのDIコンテナから渡される)
	 */
	public ChargeServiceImpl(ChargeRepository chargeRepository) {
		this.chargeRepository = chargeRepository;
	}

	/**
	 * 料金を全件取得する
	 * 
	 * @return 全料金情報
	 */
	@Override
	public List<Charge> findAll() {
		return chargeRepository.findAll();
	}

	/**
	 * 料金を1件取得する
	 * 
	 * @param memberId 料金ID
	 * @return 料金IDに一致した料金情報
	 */
	@Override
	public Optional<Charge> findById(Long chargeId) {
		return chargeRepository.findById(chargeId);
	}

	/**
	 * 料金を条件検索する
	 * 
	 * @param memberSearchCondition 料金検索条件
	 * @return 条件に一致した料金情報
	 */

	@Override
	public List<Charge> findByConditions(ChargeSearchCondition chargeSearchCondition) {
		String name = chargeSearchCondition.getName();
		if (name == null || name.isBlank()) {
			return chargeRepository.findAll(); // 全件取得
		}
		return chargeRepository.findByNameLike(name);
	}

	/**
	 * 料金を登録する
	 *
	 * @param member 登録する料金情報。 memberIdが Null であること。
	 */
	@Override
	public void add(Charge charge) {
		if (charge.getChargeId() != null) {
			throw new AttributeErrorException("料金IDが指定されていると登録できません。");
		}
		chargeRepository.add(charge);
	}

	/**
	 * 料金情報を更新する
	 * 
	 * @param member 更新する料金情報。memberId が NULL でないこと
	 */
	@Override
	public void update(Charge charge) {
		if (charge.getChargeId() == null) {
			throw new AttributeErrorException("料金IDが指定されていません。");
		}
		chargeRepository.update(charge);
	}

	/**
	 * 料金を先所する
	 * 
	 * @param memberId 料金情報のID
	 */
	@Override
	public void deleteById(Long chargeId) {
		chargeRepository.deleteById(chargeId);
	}
}

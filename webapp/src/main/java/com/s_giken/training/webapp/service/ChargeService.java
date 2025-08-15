package com.s_giken.training.webapp.service;

import java.util.List;
import java.util.Optional;

import com.s_giken.training.webapp.model.entity.Charge;
import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;

/*
 * 【ヒント】
 * ChargeService をインターフェースとして定義することで、
 * インターフェースを実体化するクラスができることを明確化する。
 */

/**
 * 料金管理機能のサービスインターフェース
 */
public interface ChargeService {
	public List<Charge> findAll();

	public Optional<Charge> findById(Long chaegeId);

	public List<Charge> findByConditions(ChargeSearchCondition chargeSearchCondition);

	public void add(Charge charge);

	public void update(Charge charge);

	public void deleteById(Long chargeId);
}

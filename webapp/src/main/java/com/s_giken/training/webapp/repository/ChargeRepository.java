package com.s_giken.training.webapp.repository;

import java.util.List;
import java.util.Optional;

import com.s_giken.training.webapp.model.entity.Charge;

public interface ChargeRepository {
	/**
	 * 料金情報をすべて取得する。
	 * 
	 * @return Chargeオブジェクトのリスト
	 */
	public List<Charge> findAll();

	/**
	 * 指定した料金IDの料金情報を取得する。
	 * 
	 * @return Optional型の Chargeオブジェクト
	 */
	public Optional<Charge> findById(Long id);

	/**
	 * 名前の一部にマッチするの料金情報リストを取得する。
	 * 
	 * @return Optional型の Chargeオブジェクト
	  */
	public List<Charge> findByNameLike(String name);

	/**
	 * 料金情報をデータベースへ登録する。
	 * 
	 * @param charge 追加するChargeオブジェクト。 chargeIdプロパティの値は null としなくてはならない
	 * @return 処理した件数
	 */
	public int add(Charge charge);

	/**
	 * データベースの料金情報を更新する。
	 * 
	 * @param charge 更新するChargeオブジェクト。 chargeIdプロパティには値が設定されている必要がある。
	 * @return 処理した件数
	 */
	public int update(Charge charge);

	/**
	 * データベースから指定した料金IDの料金情報を削除する。
	 * 
	 * @param id 料金ID
	 * @return 処理した件数
	 */
	public int deleteById(Long id);
}

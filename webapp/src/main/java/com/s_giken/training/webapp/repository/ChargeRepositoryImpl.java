package com.s_giken.training.webapp.repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.s_giken.training.webapp.model.entity.Charge;

@Repository
public class ChargeRepositoryImpl implements ChargeRepository {

	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<Charge> rowMapper;

	public ChargeRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Charge> rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	/**
	 * 料金情報をすべて取得する。
	 */
	@Override
	public List<Charge> findAll() {
		String sql = "SELECT * FROM T_CHARGE";
		return jdbcTemplate.query(sql, rowMapper);
	}

	/**
	 * 指定した料金IDの料金情報を取得する。
	 */
	@Override
	public Optional<Charge> findById(Long id) {
		String sql = "SELECT * FROM T_CHARGE WHERE charge_id = ?";
		Object[] args = { id };
		int[] argTypes = { Types.BIGINT };
		Charge charge = jdbcTemplate.queryForObject(sql, args, argTypes, rowMapper);
		return Optional.ofNullable(charge);
	}

	/**
	 * 名前の一部にマッチする料金情報リストを取得する。
	 */
	@Override
	public List<Charge> findByNameLike(String name) {
		String sql = "SELECT * FROM T_CHARGE WHERE name LIKE ?";
		Object[] args = { "%" + name + "%" };
		int[] argTypes = { Types.VARCHAR };
		return jdbcTemplate.query(sql, args, argTypes, rowMapper);
	}

	private String convertToColumnName(String sortKey) {
		return switch (sortKey) {
		case "chargeId" -> "charge_id";
		case "name" -> "name";
		case "startDate" -> "start_date";
		case "endDate" -> "end_date";
		default -> "charge_id"; // デフォルト
		};
	}

	@Override
	public List<Charge> findByNameSorted(String name, String sortKey, String orderDirection) {
		String columnName = convertToColumnName(sortKey);

		// 並び順の安全チェック
		if (!orderDirection.equalsIgnoreCase("asc") && !orderDirection.equalsIgnoreCase("desc")) {
			orderDirection = "asc";
		}

		String sql = "SELECT * FROM T_CHARGE WHERE name LIKE ? ORDER BY " + columnName + " "
				+ orderDirection;
		Object[] args = { "%" + name + "%" };
		int[] argTypes = { Types.VARCHAR };
		return jdbcTemplate.query(sql, args, argTypes, rowMapper);
	}

	/**
	 * 料金情報をデータベースへ登録する。
	 */
	@Override
	public int add(Charge charge) {
		Long chargeId = charge.getChargeId();
		if (chargeId == null) {
			chargeId = jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR t_charge_seq", Long.class);
			charge.setChargeId(chargeId);
		}

		String sql = """
				    INSERT INTO T_CHARGE (charge_id, name, amount, start_date, end_date, created_at, modified_at)
				    VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
				""";

		return jdbcTemplate.update(
				sql,
				chargeId,
				charge.getName(),
				charge.getAmount(),
				charge.getStartDate(),
				charge.getEndDate());
	}

	/**
	 * データベースの料金情報を更新する。
	 */
	@Override
	public int update(Charge charge) {
		String sql = """
				    UPDATE T_CHARGE
				    SET name = ?, amount = ?, start_date = ?, end_date = ?, modified_at = CURRENT_TIMESTAMP
				    WHERE charge_id = ?
				""";

		return jdbcTemplate.update(
				sql,
				charge.getName(),
				charge.getAmount(),
				charge.getStartDate(),
				charge.getEndDate(),
				charge.getChargeId());
	}

	/**
	 * データベースから指定した料金IDの料金情報を削除する。
	 */
	@Override
	public int deleteById(Long id) {
		String sql = "DELETE FROM T_CHARGE WHERE charge_id = ?";
		return jdbcTemplate.update(sql, id);
	}
}

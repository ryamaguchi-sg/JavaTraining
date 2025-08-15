package com.s_giken.training.webapp.repository.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.s_giken.training.webapp.model.entity.Charge;

/**
 * データベースからのT_USERデータをMemberオブジェクトにマッピングする
 * 
 * @Autowired で注入できるように、DIコンテナのコンポーネントとする。
 */
@Component
public class ChargeRowMapper implements RowMapper<Charge> {
	/**
	 * マッピング処理を行うメソッド
	 * 
	 * @param rs     データベースからのレコードセット
	 * @param rowNum 処理行数
	 * 
	 * @return Memberオブジェクト
	 */
	@Override
	public Charge mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
		Charge charge = new Charge();

		charge.setChargeId(rs.getLong("charge_id"));
		charge.setName(rs.getString("name"));
		charge.setAmount((rs.getBigDecimal("amount")));

		// データベースから取得した DATE 型は、Javaでは、java.sql.Date型となるので、
		// それを、java.util.time.LocalDate形に変換する。
		Date startDate = rs.getDate("start_date");
		charge.setStartDate((startDate != null) ? startDate.toLocalDate() : null);

		Date endDate = rs.getDate("end_date");
		charge.setEndDate((endDate != null) ? endDate.toLocalDate() : null);

		charge.setCreatedAt(rs.getTimestamp("created_at"));
		charge.setModifiedAt(rs.getTimestamp("modified_at"));

		return charge;
	}
}

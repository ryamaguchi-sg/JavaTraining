package com.s_giken.training.webapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.s_giken.training.webapp.model.entity.Account;

@Repository
public class AccountRepository {

	private final JdbcTemplate jdbcTemplate;

	public AccountRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Account findByName(String name) {
		String sql = "SELECT name, password FROM T_ACCOUNT WHERE name = ?";
		RowMapper<Account> rowMapper = (rs, rowNum) -> new Account(
				rs.getString("name"),
				rs.getString("password"));

		return jdbcTemplate.query(sql, rowMapper, name)
				.stream()
				.findFirst()
				.orElse(null);

	}
}
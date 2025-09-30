package com.s_giken.training.webapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.s_giken.training.webapp.model.entity.Account;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int add(Account account) {

		String sql = "INSERT INTO T_ACCOUNT (name, password) VALUES (?, ?)";
		return jdbcTemplate.update(sql, account.getName(), account.getPassword());

	}

}

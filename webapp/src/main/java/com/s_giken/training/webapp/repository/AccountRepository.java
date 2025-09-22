package com.s_giken.training.webapp.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.s_giken.training.webapp.model.entity.Account;

public class AccountRepository {

	private final Connection connection;

	public AccountRepository(Connection connection) {
		this.connection = connection;
	}

	public Account findByName(String name) throws SQLException {
		String sql = "SELECT name, password FROM T_ACCOUNT WHERE name = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new Account(rs.getString("name"), rs.getString("password"));
			} else {
				return null;
			}
		}
	}
}

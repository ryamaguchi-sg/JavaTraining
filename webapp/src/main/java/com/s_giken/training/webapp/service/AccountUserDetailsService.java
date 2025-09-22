package com.s_giken.training.webapp.service;

import java.sql.SQLException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.s_giken.training.webapp.model.entity.Account;
import com.s_giken.training.webapp.repository.AccountRepository;

public class AccountUserDetailsService implements UserDetailsService {

	private final AccountRepository accountRepository;

	public AccountUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Account account = accountRepository.findByName(username);
			if (account == null) {
				throw new UsernameNotFoundException("User not found");
			}

			return org.springframework.security.core.userdetails.User
					.withUsername(account.getName())
					.password(account.getPassword())
					.roles("USER")
					.build();

		} catch (SQLException e) {
			throw new UsernameNotFoundException("Database error", e);
		}
	}
}

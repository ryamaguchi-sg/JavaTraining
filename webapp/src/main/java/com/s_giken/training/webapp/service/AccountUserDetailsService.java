package com.s_giken.training.webapp.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.s_giken.training.webapp.model.entity.Account;
import com.s_giken.training.webapp.repository.AccountRepository;

@Service
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

		} catch (Exception e) {
			throw new UsernameNotFoundException("Database error", e);
		}
	}
}

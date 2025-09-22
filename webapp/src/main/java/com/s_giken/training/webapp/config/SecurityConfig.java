package com.s_giken.training.webapp.config;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import com.s_giken.training.webapp.repository.AccountRepository;
import com.s_giken.training.webapp.service.AccountUserDetailsService;

/**
 * Spring Securityの設定クラス
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	/**
	 * Spring Securityの設定
	 *
	 * @param http HttpSecurityオブジェクト
	 * @return SecurityFilterChainオブジェクト
	 * @throws Exception 例外全般
	 */
	@Bean
	SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable()) // CSRF対策を無効化
				.headers((header) -> header
						.frameOptions((frame) -> frame.disable()))
				.formLogin((form) -> form
						.defaultSuccessUrl("/")
						.loginProcessingUrl("/login")
						.loginPage("/login")
						.failureUrl("/login?error")
						.permitAll())
				.logout((logout) -> logout
						.logoutSuccessUrl("/"))
				.authorizeHttpRequests((authorize) -> authorize
						// 特例として認証を無視するURL
						.requestMatchers(
								PathPatternRequestMatcher
										.withDefaults()
										.matcher("/h2-console/**"),
								PathPatternRequestMatcher
										.withDefaults()
										.matcher("/image/**"),
								PathPatternRequestMatcher
										.withDefaults()
										.matcher("/css/**"))
						.permitAll()
						// 特例以外のURLは要認証
						.anyRequest().authenticated());

		return http.build();
	}

	/**
	 * ログインユーザー情報を設定する
	 *
	 * ※パスワードはハッシュ化せずにそのまま設定
	 *
	 * @return ログインユーザー情報
	 */
	@Bean
	UserDetailsService users(DataSource dataSource) {
		Connection connection = DataSourceUtils.getConnection(dataSource);
		AccountRepository accountRepository = new AccountRepository(connection);
		return new AccountUserDetailsService(accountRepository);
	}

	/**
	 * パスワードをBcryptでハッシュ化するオブジェクトを生成する
	 *
	 * @return パスワードをハッシュ化するエンコーダーのオブジェクト
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

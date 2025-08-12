package com.s_giken.traning.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    // Spring Securityの設定
    // ルート以下のアクセスは認証が必要
    // ログインページはカスタムの /login
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/**").authenticated()
                .anyRequest().denyAll())
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login")
                        .failureForwardUrl("/login-error")
                        .permitAll());
        return http.build();
    }

    // ログリンユーザー情報を設定する
    // ユーザ名user、パスワードpasswordでログインできるようになる。
    // ※パスワードはハッシュ化せずにそのまま設定
    @Bean
    public UserDetailsService users() {
        var user = User
                .builder()
                .username("user")
                .password("{noop}password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

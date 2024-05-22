package com.web.vop.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import com.web.vop.handler.LoginSuccessHandler;
import com.web.vop.persistence.Constant;
import com.web.vop.service.UserDetailsServiceImple;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@EnableWebSecurity(debug = true)
@Configuration
@RequiredArgsConstructor
@Log4j
public class SecurityConfig extends WebSecurityConfigurerAdapter implements Constant {

	@Autowired
	UserDetailsServiceImple userDetailsServiceImple;

	@Autowired
	LoginSuccessHandler loginSuccessHandler;

	@Autowired
	PersistentTokenRepository tokenRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("--------------------security filter load------------------");
		// form���� �ۼ��� ��û�� ������ ����ä�� tag�� name �Ӽ� ������ id, pw ����
		// ���� UserDetailsService�� �Ѱ���

		http.authorizeRequests()
			.antMatchers(PERMIT_ALL).permitAll()
			.antMatchers(MEMBER_ONLY).authenticated()
			.antMatchers(ADMIN_ONLY).hasAnyRole(AUTH_ADMIN)
			.antMatchers(SELLER_OVER).hasAnyRole(AUTH_SELLER, AUTH_ADMIN);

		http.formLogin()
			.loginPage("/member/login")
			.usernameParameter("memberId")
			.passwordParameter("memberPw")
			.loginProcessingUrl("/member/login")
			.defaultSuccessUrl("/board/main", false);

		
		http.rememberMe() 
			.key("key") 
			.rememberMeParameter("rememberMe")
			.rememberMeCookieName("rememberMe") 
			.tokenValiditySeconds(60*60*24)
			.tokenRepository(tokenRepository)
			.userDetailsService(userDetailsServiceImple)
			.authenticationSuccessHandler(loginSuccessHandler);
			

		http.logout()
			.logoutUrl("/member/logout") // �α׾ƿ� ó�� URL
			.logoutSuccessUrl("/board/main") // �α׾ƿ� ���� �� �̵� ������
			.deleteCookies("JSESSIONID", "rememberMe") // �α׾ƿ� �� ��Ű ����
			.invalidateHttpSession(true); // ���� ��ȿȭ ����

		http.csrf().disable()
			.sessionManagement() // maximumSessions, maxSessionsPreventsLogin�� �����ϱ� ���� ȣ��
			.maximumSessions(1) // �ϳ��� ���̵�� ���ÿ� �α��� �� �� �ִ� �ִ�ġ : 1
			.maxSessionsPreventsLogin(true); // �������� �ʰ��Ͽ� �α��ν�, ���� �α����� ���̵��� ���� ���� ����

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("auth check " + auth);
		auth.userDetailsService(userDetailsServiceImple).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	

}

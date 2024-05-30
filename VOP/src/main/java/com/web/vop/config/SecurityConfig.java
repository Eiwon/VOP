package com.web.vop.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.socket.WebSocketSession;

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
	SimpleUrlAuthenticationSuccessHandler loginSuccessHandler;
	
	@Autowired
	SimpleUrlLogoutSuccessHandler logoutSuccessHandler;
	
	@Autowired
	PersistentTokenRepository tokenRepository;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("--------------------security filter load------------------");
		// form으로 작성한 요청이 들어오면 가로채서 tag의 name 속성 값으로 id, pw 저장
		// 이후 UserDetailsService로 넘겨줌

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

		http.rememberMe() // 자동 로그인
			.key("key") 
			.rememberMeParameter("rememberMe")
			.rememberMeCookieName("rememberMe") 
			.tokenValiditySeconds(60*60*24)
			.tokenRepository(tokenRepository)
			.userDetailsService(userDetailsServiceImple)
			.authenticationSuccessHandler(loginSuccessHandler);
			
		http.logout()
			.logoutUrl("/member/logout") // 로그아웃 처리 URL
			.logoutSuccessUrl("/board/main")// 로그아웃 성공 후 이동 페이지
			.deleteCookies("JSESSIONID", "rememberMe") // 로그아웃 후 쿠키 삭제
			.logoutSuccessHandler(logoutSuccessHandler)
			.invalidateHttpSession(true); // 세션 무효화 설정

		http.exceptionHandling()
			.accessDeniedPage("/access/denied");

		
		http.csrf().disable()
			.sessionManagement() // maximumSessions, maxSessionsPreventsLogin을 설정하기 위해 호출
			.maximumSessions(1) // 하나의 아이디로 동시에 로그인 할 수 있는 최대치 : 1
			.maxSessionsPreventsLogin(true); // 설정값을 초과하여 로그인시, 먼저 로그인한 아이디의 세션 만료 설정

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

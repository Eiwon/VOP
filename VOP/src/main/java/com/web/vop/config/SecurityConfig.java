package com.web.vop.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import org.springframework.security.web.csrf.CsrfFilter;

import org.springframework.web.filter.CharacterEncodingFilter;

import com.web.vop.handler.LoginSuccessHandler;
import com.web.vop.handler.LogoutSuccessHandler;
import com.web.vop.handler.SecurityAccessDeniedHandler;
import com.web.vop.service.UserDetailsServiceImple;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Log4j
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // PreAuthorize 어노테이션 쓰려면 true로 설정
public class SecurityConfig extends WebSecurityConfigurerAdapter implements SecurityConfigConstants {
	
	@Autowired
	PersistentTokenRepository tokenRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("--------------------security filter load------------------");
		// form으로 작성한 요청이 들어오면 가로채서 tag의 name 속성 값으로 id, pw 저장
		// 이후 UserDetailsService로 넘겨줌
		
		http.authorizeRequests()
			.antMatchers(ANONYMOUS_ONLY).anonymous()
			.antMatchers(PERMIT_ALL).permitAll()
			.antMatchers(NORMAL_OVER).authenticated()
			.antMatchers(ADMIN_ONLY).hasAuthority(ROLE_ADMIN)
			.antMatchers(SELLER_OVER).hasRole(AUTH_SELLER)
			//.antMatchers("/membership/**").hasRole(AUTH_MEMBERSHIP)
			.expressionHandler(expressionHandler());
		// hasRole("권한") : 특정 권한이 있는지 체크 (권한 계층 적용)
		// hasAnyRole("권한1", "권한2", ...) : 목록 중 하나의 권한이라도 있는지 체크
		// hasAuthority("ROLE_권한") : 특정 권한이 있는지 체크(권한 계층 미적용)
		
		http.exceptionHandling()
		.accessDeniedHandler(securityAccessDeniedHandler());	
		
		// form 태그를 사용하여 로그인하도록 설정
		http.formLogin()
			// 로그인 페이지 설정 (안하면 security에서 제공하는 기본 페이지로 설정됨)
			.loginPage("/member/login")
			// 해당 페이지의 form 태그에서 id를 입력할 input 태그의 name
			.usernameParameter("memberId")
			// 해당 페이지의 form 태그에서 비밀번호를 입력할 input 태그의 name
			.passwordParameter("memberPw")
			// form이 제출될 URL (이 경로로 오는 post 요청을 필터에서 가로채서 id와 비밀번호로 로그인 처리함)
			.loginProcessingUrl("/member/login")
			// 로그인 성공 / 실패 했을 때 진행할 작업 (커스텀하고 싶으면 등록)
			.successHandler(loginSuccessHandler())
			.failureHandler(loginFailHandler());
		
		/* 자동로그인할 계정이 어떤 계정인지에 대한 정보는 서버와 클라이언트가 모두
		 * 가지고 있어야 합니다.
		 * 보안 때문에, 정보를 암호화한 문자열(=token)로 저장합니다
		 * */
		http.rememberMe() // 시큐리티에서 제공하는 자동 로그인 기능 사용 설정
			// 암호화할 때 사용할 key값 (아무 문자열이든 가능)
			.key("key") 
			// login form 태그에서 자동로그인 여부를 입력할 input 태그의 name
			.rememberMeParameter("rememberMe")
			// 클라이언트의 자동로그인 정보는 쿠키에 저장됨 => 쿠키 이름 설정
			.rememberMeCookieName("rememberMe") 
			// 자동로그인 유효기간
			.tokenValiditySeconds(60*60*24*3)
			// 서버의 자동로그인 정보는 DB에 저장됨 => 우리 DB에 연결된 tokenRepository를 만들어서 연결
			.tokenRepository(tokenRepository)
			.userDetailsService(userDetailsServiceImple())
			.authenticationSuccessHandler(loginSuccessHandler());
		
		http.logout()
			.logoutUrl("/member/logout") // 로그아웃 처리 URL
			.logoutSuccessUrl("/board/main")// 로그아웃 성공 후 이동 페이지
			.deleteCookies("JSESSIONID", "rememberMe") // 로그아웃 후 쿠키 삭제
			.logoutSuccessHandler(logoutSuccessHandler())
			.invalidateHttpSession(true); // 세션 무효화 설정
		
		// header 정보에 xssProtection 기능 설정 
		// (우리 사이트에서 script를 사용하여 페이지를 이동할 수 없도록 설정)
		http.headers().xssProtection().block(true);
		// 위의 XSS protection에 예외 설정
		http.headers()
			.contentSecurityPolicy("script-src " + PERMIT_SCRIPT_SRC)
			.and()
			.contentSecurityPolicy("img-src " + PERMIT_IMG_SRC);
	
		// CSRF 기능 사용시, 한글 인코딩 전에 CSRF 필터가 토큰 검사하다가 한글을 깨버림 => 인코딩 필터가 먼저 실행되도록 설정
		http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);
			
		http.sessionManagement()
			.maximumSessions(1);
		
	} // end configure

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("auth check " + auth);
		auth.userDetailsService(userDetailsServiceImple()).passwordEncoder(passwordEncoder());
	} // end configure

	// 비밀번호를 암호화하기 위한 인코더 (회원 정보 수정 기능 같은 곳에서도 써야해서 Bean으로 선언)
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	} // end passwordEncoder
	
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		return filter;
	} // end characterEncodingFilter
	
	
	public SimpleUrlAuthenticationSuccessHandler loginSuccessHandler() {
		SimpleUrlAuthenticationSuccessHandler loginSuccessHandler = new LoginSuccessHandler();
		return loginSuccessHandler;
	} // end loginSuccessHandler

	public SimpleUrlAuthenticationFailureHandler loginFailHandler() {
		SimpleUrlAuthenticationFailureHandler loginFailHandler = new SimpleUrlAuthenticationFailureHandler();
		loginFailHandler.setDefaultFailureUrl("/member/loginFail");
		return loginFailHandler;
	} // end loginFailHandler
	
	@Bean
	public UserDetailsService userDetailsServiceImple() {
		return new UserDetailsServiceImple();
	}
	
	public SimpleUrlLogoutSuccessHandler logoutSuccessHandler() {
		return new LogoutSuccessHandler();
	} // end logoutSuccessHandler
	
	public AccessDeniedHandler securityAccessDeniedHandler() {
		return new SecurityAccessDeniedHandler();
	} // end securityAccessDeniedHandler
	
	// 권한에 계층 구조 설정 (상위 권한이 하위의 모든 권한을 포함)
	public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
		DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
		RoleHierarchyImpl roleHierarchyImple = new RoleHierarchyImpl();
		roleHierarchyImple.setHierarchy(ROLE_HIERARCHY);
		expressionHandler.setRoleHierarchy(roleHierarchyImple);
		return expressionHandler;
	} // end roleVoter
	
}

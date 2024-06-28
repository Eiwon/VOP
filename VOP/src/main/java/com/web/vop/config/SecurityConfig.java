package com.web.vop.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import org.springframework.security.web.csrf.CsrfFilter;

import org.springframework.web.filter.CharacterEncodingFilter;

import com.web.vop.handler.LoginSuccessHandler;
import com.web.vop.handler.LogoutSuccessHandler;
import com.web.vop.handler.SecurityAccessDeniedHandler;
import com.web.vop.service.UserDetailsServiceImple;
import com.web.vop.util.Constant;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Log4j
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements SecurityConfigConstants {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	PersistentTokenRepository tokenRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("--------------------security filter load------------------");
		// form���� �ۼ��� ��û�� ������ ����ä�� tag�� name �Ӽ� ������ id, pw ����
		// ���� UserDetailsService�� �Ѱ���
		
		http.authorizeRequests()
			.antMatchers(ANONYMOUS_ONLY).anonymous()
			.antMatchers(PERMIT_ALL).permitAll()
			.antMatchers(NORMAL_OVER).authenticated()
			.antMatchers(ADMIN_ONLY).hasAuthority(ROLE_ADMIN)
			.antMatchers(SELLER_OVER).hasRole(AUTH_SELLER)
			//.antMatchers("/membership/**").hasRole(AUTH_MEMBERSHIP)
			.expressionHandler(expressionHandler());
		http.exceptionHandling()
			.accessDeniedHandler(securityAccessDeniedHandler());	
		// hasRole("����") : Ư�� ������ �ִ��� üũ (���� ���� ����)
		// hasAnyRole("����1", "����2", ...) : ��� �� �ϳ��� �����̶� �ִ��� üũ
		// hasAuthority("ROLE_����") : Ư�� ������ �ִ��� üũ(���� ���� ������)
		http.formLogin()
			.loginPage("/member/login")
			.usernameParameter("memberId")
			.passwordParameter("memberPw")
			.loginProcessingUrl("/member/login")
			.successHandler(loginSuccessHandler())
			.failureHandler(loginFailHandler());
		
		http.rememberMe() // �ڵ� �α���
			.key("key") 
			.rememberMeParameter("rememberMe")
			.rememberMeCookieName("rememberMe") 
			.tokenValiditySeconds(60*60*24*3)
			.tokenRepository(tokenRepository)
			.userDetailsService(userDetailsService)
			.authenticationSuccessHandler(loginSuccessHandler());
		
		http.logout()
			.logoutUrl("/member/logout") // �α׾ƿ� ó�� URL
			.logoutSuccessUrl("/board/main")// �α׾ƿ� ���� �� �̵� ������
			.deleteCookies("JSESSIONID", "rememberMe") // �α׾ƿ� �� ��Ű ����
			.logoutSuccessHandler(logoutSuccessHandler())
			.invalidateHttpSession(true); // ���� ��ȿȭ ����
		
		// header ������ xssProtection ��� ����
		http.headers().xssProtection().block(true);
		http.headers()
			.contentSecurityPolicy("script-src " + PERMIT_SCRIPT_SRC)
			.and()
			.contentSecurityPolicy("img-src " + PERMIT_IMG_SRC);
	
		http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);
			
		http.sessionManagement()
			.maximumSessions(1);
		
	} // end configure

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("auth check " + auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	} // end configure

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
	
//	public UserDetailsService userDetailsServiceImple() {
//		return new UserDetailsServiceImple();
//	}
	
	public SimpleUrlLogoutSuccessHandler logoutSuccessHandler() {
		return new LogoutSuccessHandler();
	} // end logoutSuccessHandler
	
	public AccessDeniedHandler securityAccessDeniedHandler() {
		return new SecurityAccessDeniedHandler();
	} // end securityAccessDeniedHandler
	
	// ���ѿ� ���� ���� ���� (���� ������ ������ ��� ������ ����)
	public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
		DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
		RoleHierarchyImpl roleHierarchyImple = new RoleHierarchyImpl();
		roleHierarchyImple.setHierarchy(ROLE_HIERARCHY);
		expressionHandler.setRoleHierarchy(roleHierarchyImple);
		return expressionHandler;
	} // end roleVoter
	
}

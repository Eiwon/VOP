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
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // PreAuthorize ������̼� ������ true�� ����
public class SecurityConfig extends WebSecurityConfigurerAdapter implements SecurityConfigConstants {
	
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
		// hasRole("����") : Ư�� ������ �ִ��� üũ (���� ���� ����)
		// hasAnyRole("����1", "����2", ...) : ��� �� �ϳ��� �����̶� �ִ��� üũ
		// hasAuthority("ROLE_����") : Ư�� ������ �ִ��� üũ(���� ���� ������)
		
		http.exceptionHandling()
		.accessDeniedHandler(securityAccessDeniedHandler());	
		
		// form �±׸� ����Ͽ� �α����ϵ��� ����
		http.formLogin()
			// �α��� ������ ���� (���ϸ� security���� �����ϴ� �⺻ �������� ������)
			.loginPage("/member/login")
			// �ش� �������� form �±׿��� id�� �Է��� input �±��� name
			.usernameParameter("memberId")
			// �ش� �������� form �±׿��� ��й�ȣ�� �Է��� input �±��� name
			.passwordParameter("memberPw")
			// form�� ����� URL (�� ��η� ���� post ��û�� ���Ϳ��� ����ä�� id�� ��й�ȣ�� �α��� ó����)
			.loginProcessingUrl("/member/login")
			// �α��� ���� / ���� ���� �� ������ �۾� (Ŀ�����ϰ� ������ ���)
			.successHandler(loginSuccessHandler())
			.failureHandler(loginFailHandler());
		
		/* �ڵ��α����� ������ � ���������� ���� ������ ������ Ŭ���̾�Ʈ�� ���
		 * ������ �־�� �մϴ�.
		 * ���� ������, ������ ��ȣȭ�� ���ڿ�(=token)�� �����մϴ�
		 * */
		http.rememberMe() // ��ť��Ƽ���� �����ϴ� �ڵ� �α��� ��� ��� ����
			// ��ȣȭ�� �� ����� key�� (�ƹ� ���ڿ��̵� ����)
			.key("key") 
			// login form �±׿��� �ڵ��α��� ���θ� �Է��� input �±��� name
			.rememberMeParameter("rememberMe")
			// Ŭ���̾�Ʈ�� �ڵ��α��� ������ ��Ű�� ����� => ��Ű �̸� ����
			.rememberMeCookieName("rememberMe") 
			// �ڵ��α��� ��ȿ�Ⱓ
			.tokenValiditySeconds(60*60*24*3)
			// ������ �ڵ��α��� ������ DB�� ����� => �츮 DB�� ����� tokenRepository�� ���� ����
			.tokenRepository(tokenRepository)
			.userDetailsService(userDetailsServiceImple())
			.authenticationSuccessHandler(loginSuccessHandler());
		
		http.logout()
			.logoutUrl("/member/logout") // �α׾ƿ� ó�� URL
			.logoutSuccessUrl("/board/main")// �α׾ƿ� ���� �� �̵� ������
			.deleteCookies("JSESSIONID", "rememberMe") // �α׾ƿ� �� ��Ű ����
			.logoutSuccessHandler(logoutSuccessHandler())
			.invalidateHttpSession(true); // ���� ��ȿȭ ����
		
		// header ������ xssProtection ��� ���� 
		// (�츮 ����Ʈ���� script�� ����Ͽ� �������� �̵��� �� ������ ����)
		http.headers().xssProtection().block(true);
		// ���� XSS protection�� ���� ����
		http.headers()
			.contentSecurityPolicy("script-src " + PERMIT_SCRIPT_SRC)
			.and()
			.contentSecurityPolicy("img-src " + PERMIT_IMG_SRC);
	
		// CSRF ��� ����, �ѱ� ���ڵ� ���� CSRF ���Ͱ� ��ū �˻��ϴٰ� �ѱ��� ������ => ���ڵ� ���Ͱ� ���� ����ǵ��� ����
		http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);
			
		http.sessionManagement()
			.maximumSessions(1);
		
	} // end configure

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("auth check " + auth);
		auth.userDetailsService(userDetailsServiceImple()).passwordEncoder(passwordEncoder());
	} // end configure

	// ��й�ȣ�� ��ȣȭ�ϱ� ���� ���ڴ� (ȸ�� ���� ���� ��� ���� �������� ����ؼ� Bean���� ����)
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
	
	// ���ѿ� ���� ���� ���� (���� ������ ������ ��� ������ ����)
	public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
		DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
		RoleHierarchyImpl roleHierarchyImple = new RoleHierarchyImpl();
		roleHierarchyImple.setHierarchy(ROLE_HIERARCHY);
		expressionHandler.setRoleHierarchy(roleHierarchyImple);
		return expressionHandler;
	} // end roleVoter
	
}

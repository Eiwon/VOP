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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.web.vop.handler.LoginSuccessHandler;
import com.web.vop.service.UserDetailsServiceImple;
import com.web.vop.util.Constant;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Log4j
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements Constant {

	@Autowired
	UserDetailsServiceImple userDetailsServiceImple;
	
	@Autowired
	SimpleUrlAuthenticationSuccessHandler loginSuccessHandler;
	
	@Autowired
	SimpleUrlAuthenticationFailureHandler loginFailHandler;
	
	@Autowired
	SimpleUrlLogoutSuccessHandler logoutSuccessHandler;
	
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
			.expressionHandler(expressionHandler());
		// hasRole("����") : Ư�� ������ �ִ��� üũ (���� ���� ����)
		// hasAnyRole("����1", "����2", ...) : ��� �� �ϳ��� �����̶� �ִ��� üũ
		// hasAuthority("ROLE_����") : Ư�� ������ �ִ��� üũ(���� ���� ������)
		
		http.formLogin()
			.loginPage("/member/login")
			.usernameParameter("memberId")
			.passwordParameter("memberPw")
			.loginProcessingUrl("/member/login")
			.successHandler(loginSuccessHandler)
			.failureHandler(loginFailHandler);

		http.rememberMe() // �ڵ� �α���
			.key("key") 
			.rememberMeParameter("rememberMe")
			.rememberMeCookieName("rememberMe") 
			.tokenValiditySeconds(60*60*24)
			.tokenRepository(tokenRepository)
			.userDetailsService(userDetailsServiceImple)
			.authenticationSuccessHandler(loginSuccessHandler);
		
		http.logout()
			.logoutUrl("/member/logout") // �α׾ƿ� ó�� URL
			.logoutSuccessUrl("/board/main")// �α׾ƿ� ���� �� �̵� ������
			.deleteCookies("JSESSIONID", "rememberMe") // �α׾ƿ� �� ��Ű ����
			.logoutSuccessHandler(logoutSuccessHandler)
			.invalidateHttpSession(true); // ���� ��ȿȭ ����
		
		http.exceptionHandling()
			.accessDeniedPage("/access/denied");

		http.csrf().disable();
			//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		//http.headers().cacheControl();
		
	} // end configure

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("auth check " + auth);
		auth.userDetailsService(userDetailsServiceImple).passwordEncoder(passwordEncoder());
	} // end configure

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	} // end passwordEncoder
	
	@Bean
	public JWTAuthenticationFilter jwtAuthenticationFilter() {
		return new JWTAuthenticationFilter();
	} // end jwtAuthenticationFilter
	
	@Bean
	public SimpleUrlAuthenticationSuccessHandler loginSuccessHandler() {
		SimpleUrlAuthenticationSuccessHandler loginSuccessHandler = new LoginSuccessHandler();
		loginSuccessHandler.setUseReferer(true);
		loginSuccessHandler.setDefaultTargetUrl("/board/main");
		return loginSuccessHandler;
	} // end loginSuccessHandler

	@Bean
	public SimpleUrlAuthenticationFailureHandler loginFailHandler() {
		SimpleUrlAuthenticationFailureHandler loginFailHandler = new SimpleUrlAuthenticationFailureHandler();
		loginFailHandler.setDefaultFailureUrl("/member/loginFail");
		return loginFailHandler;
	} // end loginFailHandler
	
	// ���ѿ� ���� ���� ���� (���� ������ ������ ��� ������ ����)
	@Bean
	public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
		DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
		RoleHierarchyImpl roleHierarchyImple = new RoleHierarchyImpl();
		roleHierarchyImple.setHierarchy(ROLE_HIERARCHY);
		expressionHandler.setRoleHierarchy(roleHierarchyImple);
		return expressionHandler;
	} // end roleVoter
	
}

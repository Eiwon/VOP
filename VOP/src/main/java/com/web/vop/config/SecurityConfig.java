package com.web.vop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.web.vop.service.UserDetailsServiceImple;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Log4j
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	UserDetailsServiceImple userDetailsServiceImple;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("--------------------security filter-------------------");
		// form으로 작성한 요청이 들어오면 가로채서 tag의 name 속성 값으로 id, pw 저장
		// 이후 UserDetailsService로 넘겨줌
		http
        .authorizeRequests()
            .anyRequest().authenticated()
        .and()
            .formLogin()
            .loginPage("/member/login")
            .usernameParameter("memberId")
            .passwordParameter("memberPw")
            .loginProcessingUrl("/member/login")
            .defaultSuccessUrl("/member/register", false)
            .permitAll()
        .and()
            .logout()
        .and()
        	.csrf().disable();
							
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("auth check " + auth);
	}
	
	
	
	
	
}

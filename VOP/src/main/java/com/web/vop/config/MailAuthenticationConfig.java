package com.web.vop.config;

import java.util.HashMap;

import java.util.Map;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailAuthenticationConfig {
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		
		// ApiKey 파일에 아래 코드 넣어주면 됩니다 
		// 이메일은 본인 이메일로 변경해도 되고, 앱 비밀번호(MAIL_AUTH_PW) 발급받는 법 https://dorothy-yang.tistory.com/179
		
		// 이메일 인증시, 해당 id와 비밀번호로 구글에 로그인하여 인증 메일을 보냄
		// public static final String MAIL_AUTH_ID = "rtst606@gmail.com";
		// public static final String MAIL_AUTH_PW = "mslbwxavcqmhztas";
		
//		javaMailSender.setUsername(ApiKey.MAIL_AUTH_ID); 에러 발생해서 주석 처리 했습니다.
//		javaMailSender.setPassword(ApiKey.MAIL_AUTH_PW); 에러 발생해서 주석 처리 했습니다.
		javaMailSender.setHost("smtp.gmail.com");
		Properties property = new Properties();
		property.put("mail.smtp.auth", true);
		property.put("mail.transport.protocol", "smtp");
		property.put("mail.smtp.starttls.enable", true);
		property.put("mail.smtp.starttls.required", true);
		javaMailSender.setJavaMailProperties(property);
		
		return javaMailSender;
	} // end javaMailSender
	
	@Bean
	public Map<String, String> emailAuthMap(){
		return new HashMap<>();
	} // end emailAuthMap
}

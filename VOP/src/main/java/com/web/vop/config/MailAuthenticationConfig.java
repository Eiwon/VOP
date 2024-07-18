package com.web.vop.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.web.vop.util.MailAuthenticationUtil;

@Configuration
public class MailAuthenticationConfig {

	// JavaMail API를 사용하여 gmail smtp 서버를 통해 메일 송신
	// SMTP : Simple Mail Transfer Protocol, 대표적인 메일 전송용 프로토콜
	@Bean
	public JavaMailSender javaMailSender() {
		
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setUsername(ApiKey.MAIL_AUTH_ID); 
		javaMailSender.setPassword(ApiKey.MAIL_AUTH_PW);

		javaMailSender.setHost("smtp.gmail.com"); // gmail smtp 서버 주소
		javaMailSender.setPort(587); // SMTP는 원래 25번 포트를 사용하지만 EC2에서는 25번을 사용할 수 없어서 587번으로 변경
		 
		Properties property = new Properties();
		property.put("mail.smtp.auth", true); // username과 password로 smtp 서버에 로그인 시도하도록 설정
		property.put("mail.transport.protocol", "smtp");
		property.put("mail.smtp.starttls.enable", true); // transport layer security를 사용하도록 설정
		property.put("mail.smtp.starttls.required", true); // tls를 사용할 수 없으면 실패하도록 설정
		property.put("mail.smtp.port", "587");
		javaMailSender.setJavaMailProperties(property);
		
		return javaMailSender;
	} // end javaMailSender
	
	@Bean
	public MailAuthenticationUtil mailAuthenticationUtil() {
		return new MailAuthenticationUtil();
	} // end mailAuthenticationUtil
	
	
}



//@Configuration
//public class MailAuthenticationConfig {
//	
//	@Bean
//	public MailAuthenticationUtil mailAuthenticationUtil() {
//		return new MailAuthenticationUtil();
//	} // end mailAuthenticationUtil
//	
//}


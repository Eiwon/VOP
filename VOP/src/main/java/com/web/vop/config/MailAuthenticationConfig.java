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
	// == 지정한 계정으로 google에 로그인해서 메일 보내는 기능입니다
	// 해당 계정은 보안 인증 2단계 설정되어 있어야 하고, 앱 비밀번호를 설정해야 합니다
	
	// 메일을 보내주는 JavaMailSender 객체 설정
	/* 설정은 변경될 일도 없는데 메일 보낼 때마다 설정할 필요는 없으니 
	 한번만 설정하고 재활용하기 위해 Bean으로 생성
	*/
	@Bean
	public JavaMailSender javaMailSender() {
		
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		// 메일 보낼 때 사용할 google 아이디와 앱 비밀번호
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
	
	// 위의 인스턴스는 메일을 보내기만 하고 인증 기능은 없으니 
	// 인증 기능을 구현할 클래스를 생성하고, 계속 재사용하기 위해 Bean으로 설정
	@Bean
	public MailAuthenticationUtil mailAuthenticationUtil() {
		return new MailAuthenticationUtil();
	} // end mailAuthenticationUtil
	
	
}



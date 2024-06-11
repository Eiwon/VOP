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
		
		// ApiKey ���Ͽ� �Ʒ� �ڵ� �־��ָ� �˴ϴ� 
		// �̸����� ���� �̸��Ϸ� �����ص� �ǰ�, �� ��й�ȣ(MAIL_AUTH_PW) �߱޹޴� �� https://dorothy-yang.tistory.com/179
		
		// �̸��� ������, �ش� id�� ��й�ȣ�� ���ۿ� �α����Ͽ� ���� ������ ����
		// public static final String MAIL_AUTH_ID = "rtst606@gmail.com";
		// public static final String MAIL_AUTH_PW = "mslbwxavcqmhztas";
		
//		javaMailSender.setUsername(ApiKey.MAIL_AUTH_ID); ���� �߻��ؼ� �ּ� ó�� �߽��ϴ�.
//		javaMailSender.setPassword(ApiKey.MAIL_AUTH_PW); ���� �߻��ؼ� �ּ� ó�� �߽��ϴ�.
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

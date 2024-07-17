package com.web.vop.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.web.vop.util.MailAuthenticationUtil;

@Configuration
public class MailAuthenticationConfig {

	// JavaMail API�� ����Ͽ� gmail smtp ������ ���� ���� �۽�
	@Bean
	public JavaMailSender javaMailSender() {
		
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setUsername(ApiKey.MAIL_AUTH_ID); 
		javaMailSender.setPassword(ApiKey.MAIL_AUTH_PW);

		javaMailSender.setHost("smtp.gmail.com");
		javaMailSender.setPort(587);
		 
		Properties property = new Properties();
		property.put("mail.smtp.auth", true);
		property.put("mail.transport.protocol", "smtp");
		property.put("mail.smtp.starttls.enable", true);
		property.put("mail.smtp.starttls.required", true);
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


package com.web.vop.config;

import java.util.HashMap;

import java.util.Map;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

<<<<<<< HEAD
@Configuration
public class MailAuthenticationConfig {
	
	// 이메일 인증용 아이디와 비밀번호
		public static final String MAIL_AUTH_ID = "rtst606@gmail.com";
		public static final String MAIL_AUTH_PW = "mslbwxavcqmhztas";
	
	@Bean
	public JavaMailSender javaMailSender() {
		
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setUsername(ApiKey.MAIL_AUTH_ID);
		javaMailSender.setPassword(ApiKey.MAIL_AUTH_PW);

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
=======
import com.web.vop.util.MailAuthenticationUtil;

//@Configuration
//public class MailAuthenticationConfig {
//	
//	@Bean
//	public MailAuthenticationUtil mailAuthenticationUtil() {
//		return new MailAuthenticationUtil();
//	} // end mailAuthenticationUtil
//	
//}
>>>>>>> 84d86db430c1a96b7705ea713bb224188ba0c2f6

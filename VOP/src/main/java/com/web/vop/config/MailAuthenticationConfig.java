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
	// SMTP : Simple Mail Transfer Protocol, ��ǥ���� ���� ���ۿ� ��������
	// == ������ �������� google�� �α����ؼ� ���� ������ ����Դϴ�
	// �ش� ������ ���� ���� 2�ܰ� �����Ǿ� �־�� �ϰ�, �� ��й�ȣ�� �����ؾ� �մϴ�
	
	// ������ �����ִ� JavaMailSender ��ü ����
	/* ������ ����� �ϵ� ���µ� ���� ���� ������ ������ �ʿ�� ������ 
	 �ѹ��� �����ϰ� ��Ȱ���ϱ� ���� Bean���� ����
	*/
	@Bean
	public JavaMailSender javaMailSender() {
		
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		// ���� ���� �� ����� google ���̵�� �� ��й�ȣ
		javaMailSender.setUsername(ApiKey.MAIL_AUTH_ID); 
		javaMailSender.setPassword(ApiKey.MAIL_AUTH_PW);

		javaMailSender.setHost("smtp.gmail.com"); // gmail smtp ���� �ּ�
		javaMailSender.setPort(587); // SMTP�� ���� 25�� ��Ʈ�� ��������� EC2������ 25���� ����� �� ��� 587������ ����
		 
		Properties property = new Properties();
		property.put("mail.smtp.auth", true); // username�� password�� smtp ������ �α��� �õ��ϵ��� ����
		property.put("mail.transport.protocol", "smtp");
		property.put("mail.smtp.starttls.enable", true); // transport layer security�� ����ϵ��� ����
		property.put("mail.smtp.starttls.required", true); // tls�� ����� �� ������ �����ϵ��� ����
		property.put("mail.smtp.port", "587");
		javaMailSender.setJavaMailProperties(property);
		
		return javaMailSender;
	} // end javaMailSender
	
	// ���� �ν��Ͻ��� ������ �����⸸ �ϰ� ���� ����� ������ 
	// ���� ����� ������ Ŭ������ �����ϰ�, ��� �����ϱ� ���� Bean���� ����
	@Bean
	public MailAuthenticationUtil mailAuthenticationUtil() {
		return new MailAuthenticationUtil();
	} // end mailAuthenticationUtil
	
	
}



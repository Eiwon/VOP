package com.web.vop.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.web.vop.config.ApiKey;

import lombok.extern.log4j.Log4j;

@Log4j
public class MailAuthenticationUtil {
	
	// ���Ϸ� ���� ������ȣ�� ������ �Է��� ������ �����ϱ� ���� ��
	// key : email, value : �߼۵� ���� ��
	
	private Map<String, String> emailAuthMap;
	
	public JavaMailSenderImpl javaMailSender;
	
	public MailAuthenticationUtil() {
		
		emailAuthMap = new HashMap<>();
		
		javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setUsername(ApiKey.MAIL_AUTH_ID);
		javaMailSender.setPassword(ApiKey.MAIL_AUTH_PW);
		javaMailSender.setHost("smtp.gmail.com");
		Properties property = new Properties();
		property.put("mail.smtp.auth", true);
		property.put("mail.transport.protocol", "smtp");
		property.put("mail.smtp.starttls.enable", true);
		property.put("mail.smtp.starttls.required", true);
		javaMailSender.setJavaMailProperties(property);
			
	} // end MailAuthenticationUtil
	
	public void sendAuthEmail(String email) {
		log.info("�������� �̸��� �۽� to " + email);
		SimpleMailMessage message = new SimpleMailMessage();
		String authCode = generateCode();
		message.setSubject("VOP ���� ���� ��û");
		message.setText("��й�ȣ Ȯ�� ���� ��ȣ : " + authCode);
		emailAuthMap.put(email, authCode);
		message.setTo(email);
		
		javaMailSender.send(message);
		
	} // end sendAuthEmail
	
	private String generateCode() {
		double randomNum = Math.random();
		String code = String.valueOf(randomNum).substring(2, 8);
		return code;
	} // end generateCode

	public boolean verifyAuthCode (String email, String authCode) {
		log.info("�������� �̸��� ����");
		String keyCode = emailAuthMap.get(email);
		boolean res = keyCode.equals(authCode);
		if(res) {
			emailAuthMap.remove(email);
		}
		
		return res;
	} // end verifyAuthNum
	
}

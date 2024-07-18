package com.web.vop.util;

import java.util.Calendar;
import java.util.Date;
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
import com.web.vop.domain.EmailAuthenticationToken;

import lombok.extern.log4j.Log4j;

@Log4j
public class MailAuthenticationUtil {
	
	// ���Ϸ� ���� ������ȣ�� ������ �Է��� ������ �����ϱ� ���� ��
	// key : email, value : �߼۵� ���� �� + ����ð�
	private Map<String, EmailAuthenticationToken> emailAuthMap;
	
	@Autowired
	public JavaMailSender javaMailSender;
	
	public MailAuthenticationUtil() {
		
		emailAuthMap = new HashMap<>();
		
			
	} // end MailAuthenticationUtil
	
	public void sendEmail(String email, String title, String content) {
		log.info("�Ϲ� �̸��� �۽� to " + email);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(title);
		message.setText(content);
		message.setTo(email);
		javaMailSender.send(message);
	}
	
	public void sendAuthEmail(String email) {
		log.info("�������� �̸��� �۽� to " + email);
		SimpleMailMessage message = new SimpleMailMessage();
		EmailAuthenticationToken token = new EmailAuthenticationToken();
		token.setAuthCode(generateCode());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, 3);
		Date expireTime = calendar.getTime();
		
		token.setExpireTime(expireTime);
		
		message.setSubject("VOP ���� ���� ��û");
		message.setText("��й�ȣ Ȯ�� ���� ��ȣ : " + token.getAuthCode());
		message.setTo(email);
		emailAuthMap.put(email, token);
		
		log.info("�������� �ڵ�, ���� �ð� : " + token);
		log.info("message : " + message);
		javaMailSender.send(message);
		
	} // end sendAuthEmail
	
	private String generateCode() {
		double randomNum = Math.random();
		String code = String.valueOf(randomNum).substring(2, 8);
		return code;
	} // end generateCode

	// ���� ���� : return 100
	// ����� ���� ��ȣ : return 101
	// ���� ��ȣ ����ġ : return 102
	// ���� ��ȣ �̹߱� ���� : return 103
	public int verifyAuthCode (String email, String authCode) {
		log.info("�������� �̸��� ���� : " + email + ", " + authCode);
		EmailAuthenticationToken token = emailAuthMap.get(email);
		
		if(token == null) {
			return 103;
		}
		
		if(new Date().after(token.getExpireTime())) {
			emailAuthMap.remove(email);
			return 101;
		}

		boolean res = token.getAuthCode().equals(authCode);
		if(res) {
			emailAuthMap.remove(email);
			return 100;
		}else {
			return 102;
		}
	} // end verifyAuthNum
	
}

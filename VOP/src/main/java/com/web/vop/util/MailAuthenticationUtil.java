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
	
	// 메일로 보낸 인증번호를 유저가 입력할 때까지 저장하기 위한 맵
	// key : email, value : 발송된 인증 값 + 만료시간
	private Map<String, EmailAuthenticationToken> emailAuthMap;
	
	@Autowired
	public JavaMailSender javaMailSender;
	
	public MailAuthenticationUtil() {
		
		emailAuthMap = new HashMap<>();
		
			
	} // end MailAuthenticationUtil
	
	public void sendEmail(String email, String title, String content) {
		log.info("일반 이메일 송신 to " + email);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(title);
		message.setText(content);
		message.setTo(email);
		javaMailSender.send(message);
	}
	
	public void sendAuthEmail(String email) {
		log.info("본인인증 이메일 송신 to " + email);
		SimpleMailMessage message = new SimpleMailMessage();
		EmailAuthenticationToken token = new EmailAuthenticationToken();
		token.setAuthCode(generateCode());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, 3);
		Date expireTime = calendar.getTime();
		
		token.setExpireTime(expireTime);
		
		message.setSubject("VOP 본인 인증 요청");
		message.setText("비밀번호 확인 인증 번호 : " + token.getAuthCode());
		message.setTo(email);
		emailAuthMap.put(email, token);
		
		log.info("본인인증 코드, 만료 시간 : " + token);
		log.info("message : " + message);
		javaMailSender.send(message);
		
	} // end sendAuthEmail
	
	private String generateCode() {
		double randomNum = Math.random();
		String code = String.valueOf(randomNum).substring(2, 8);
		return code;
	} // end generateCode

	// 인증 성공 : return 100
	// 만료된 인증 번호 : return 101
	// 인증 번호 불일치 : return 102
	// 인증 번호 미발급 유저 : return 103
	public int verifyAuthCode (String email, String authCode) {
		log.info("본인인증 이메일 검증 : " + email + ", " + authCode);
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

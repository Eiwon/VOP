package com.web.vop.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MailAuthenticationService {
	
	// 메일로 보낸 인증번호를 유저가 입력할 때까지 저장하기 위한 맵
	// key : email, value : 발송된 인증 값
	@Autowired
	private Map<String, String> emailAuthMap;
	
	@Autowired
	public JavaMailSender javaMailSender;
	
	public void sendAuthEmail(String email) {
		log.info("본인인증 이메일 송신 to " + email);
		SimpleMailMessage message = new SimpleMailMessage();
		String authCode = "1234";
		message.setSubject("VOP 본인 인증 요청");
		message.setText("비밀번호 확인 인증 번호 : " + authCode);
		emailAuthMap.put(email, authCode);
		message.setTo(email);
		
		javaMailSender.send(message);
		
	} // end sendAuthEmail
	
	public boolean verifyAuthCode (String email, String authCode) {
		log.info("본인인증 이메일 검증");
		String keyCode = emailAuthMap.get(email);
		boolean res = keyCode.equals(authCode);
		if(res) {
			emailAuthMap.remove(email);
		}
		
		return res;
	} // end verifyAuthNum
	
}

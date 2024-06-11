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
	
	// ���Ϸ� ���� ������ȣ�� ������ �Է��� ������ �����ϱ� ���� ��
	// key : email, value : �߼۵� ���� ��
	@Autowired
	private Map<String, String> emailAuthMap;
	
	@Autowired
	public JavaMailSender javaMailSender;
	
	public void sendAuthEmail(String email) {
		log.info("�������� �̸��� �۽� to " + email);
		SimpleMailMessage message = new SimpleMailMessage();
		String authCode = "1234";
		message.setSubject("VOP ���� ���� ��û");
		message.setText("��й�ȣ Ȯ�� ���� ��ȣ : " + authCode);
		emailAuthMap.put(email, authCode);
		message.setTo(email);
		
		javaMailSender.send(message);
		
	} // end sendAuthEmail
	
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

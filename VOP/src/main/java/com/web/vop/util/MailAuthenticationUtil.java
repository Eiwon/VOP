package com.web.vop.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.web.vop.domain.EmailAuthenticationToken;

import lombok.extern.log4j.Log4j;

// �̸��� ���� ����� ������ Ŭ����
@Log4j
public class MailAuthenticationUtil {
	
	/* ���Ϸ� ������ȣ�� ������, �ùٸ� ��ȣ���� �����ϱ� ���ؼ� 
	 * ���� �ּҿ� ���� ��ȣ�� �����ص־� �ؼ� Map�� �����߽��ϴ�.
	 * �̸��Ϸ� ������ȣ�� �˻��Ҳ��� key�� �̸���, value�� ������ȣ
	 * Map�̱� ������ �ϳ��� �̸��Ϸ� ������ ������ȣ�� ������ ������ �����͸� ����˴ϴ�.
	 */
	// key : email, value : �߼۵� ���� �� + ����ð�(�Բ� �����ϱ� ���� Ŭ������ ���� ����)
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
		token.setExpireTime(generateExpireTime());
		/* ���⿡ ������ȣ�� ���� �ð��� �����ϴ� �ڵ带 �ۼ��ص� ������ ���� ���� �Լ��� �ۼ��ϴ� ���̱� ������
		 * ������ȣ�� ������ִ� �Լ��� �ִٰ� �����ϰ� ���� ���� �Լ����� ���� �ۼ��� ��
		 * ������ȣ ���� �Լ��� �ۼ��߽��ϴ�
		 */
		
		message.setSubject("VOP ���� ���� ��û");
		message.setText("��й�ȣ Ȯ�� ���� ��ȣ : " + token.getAuthCode());
		message.setTo(email);
		emailAuthMap.put(email, token);
		
		log.info("�������� �ڵ�, ���� �ð� : " + token);
		log.info("message : " + message);
		javaMailSender.send(message);
		
	} // end sendAuthEmail
	
	// ���⿡���� ������ ���� �޼ҵ��̱� ������ private���� ����
	private Date generateExpireTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, 3);
		return calendar.getTime();
	} // end generateExpireTime
	
	// ���⿡���� ������ ���� �޼ҵ��̱� ������ private���� ����
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

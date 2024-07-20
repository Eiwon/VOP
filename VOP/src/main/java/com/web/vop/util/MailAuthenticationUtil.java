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

// 이메일 인증 기능을 구현할 클래스
@Log4j
public class MailAuthenticationUtil {
	
	/* 메일로 인증번호를 보내고, 올바른 번호인지 검증하기 위해선 
	 * 메일 주소와 인증 번호를 저장해둬야 해서 Map에 저장했습니다.
	 * 이메일로 인증번호를 검색할꺼라 key는 이메일, value는 인증번호
	 * Map이기 때문에 하나의 이메일로 여러번 인증번호를 보내도 마지막 데이터만 저장됩니다.
	 */
	// key : email, value : 발송된 인증 값 + 만료시간(함께 저장하기 위한 클래스를 만들어서 설정)
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
		token.setExpireTime(generateExpireTime());
		/* 여기에 인증번호와 만료 시간을 생성하는 코드를 작성해도 되지만 메일 전송 함수를 작성하던 중이기 때문에
		 * 인증번호를 만들어주는 함수가 있다고 가정하고 메일 전송 함수부터 마저 작성한 후
		 * 인증번호 생성 함수를 작성했습니다
		 */
		
		message.setSubject("VOP 본인 인증 요청");
		message.setText("비밀번호 확인 인증 번호 : " + token.getAuthCode());
		message.setTo(email);
		emailAuthMap.put(email, token);
		
		log.info("본인인증 코드, 만료 시간 : " + token);
		log.info("message : " + message);
		javaMailSender.send(message);
		
	} // end sendAuthEmail
	
	// 여기에서만 쓰려고 만든 메소드이기 때문에 private으로 설정
	private Date generateExpireTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, 3);
		return calendar.getTime();
	} // end generateExpireTime
	
	// 여기에서만 쓰려고 만든 메소드이기 때문에 private으로 설정
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

package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.service.MemberService;
import com.web.vop.util.MailAuthenticationUtil;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/member")
@Log4j
public class MemberRESTController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MailAuthenticationUtil mailAuthService;

	@Autowired
	UserDetailsService userDetailsService;
	
	@GetMapping("/idDupChk") // id �ߺ� üũ
	public ResponseEntity<Integer> checkIdDup(String memberId){
		log.info("member id �ߺ� üũ" + memberId);
		String checkedId = memberService.checkIdDup(memberId);
		
		int result = (checkedId == null) ? 1 : 0;
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	} // end checkIdDup
	
	@GetMapping("/phoneDupChk") // �޴��� ��ȣ�� ���Կ��� üũ
	public ResponseEntity<Integer> checkPhoneDup(String memberPhone){
		log.info("member phone �ߺ� üũ : " + memberPhone);
		String checkedId = memberService.getIdByPhone(memberPhone);
		
		int result = (checkedId == null) ? 1 : 0;
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	} // end checkPhoneDup
	
//	@PostMapping("/findByPhone")
//	public ResponseEntity<String> findByNameAndPhone( // �̸�, ��ȭ��ȣ�� id ã��
//			@RequestParam String memberName, @RequestParam String memberPhone){
//		log.info("�̸�, ��ȭ��ȣ�� id ã��");
//		String checkedId = memberService.findByNameAndPhone(memberName, memberPhone);
//		
//		return new ResponseEntity<String>(checkedId, HttpStatus.OK);
//	} // end findByNameAndPhone
	
	@PostMapping("/check")
	public ResponseEntity<Boolean> checkMember(@AuthenticationPrincipal UserDetails memberDetails, String memberPw){
		log.info("��й�ȣ Ȯ��");
		boolean comp = memberService.checkLogin(memberDetails.getUsername(), memberPw);
		
		return new ResponseEntity<Boolean>(comp, HttpStatus.OK);
	} // end checkMember
	
	
	@PutMapping("/auth")
	public ResponseEntity<Integer> updateAuth(
			@RequestParam String memberId, @RequestParam String auth){
		log.info("���� ����");
		int res = memberService.updateAuth(memberId, auth);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateAuth
	
	@GetMapping("/mailAuthentication")
	public ResponseEntity<Integer> mailAuthenticationGET(String email){
		log.info("�̸��� ���� ��û : " + email);
		int res = 0;
		
		mailAuthService.sendAuthEmail(email);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end mailAuthenticationGET
	
	@GetMapping("/reload")
	public ResponseEntity<Integer> reloadAuth(@AuthenticationPrincipal UserDetails memberDetails){
		log.info("���� �ֽ�ȭ");
		int res = 1;
		UserDetails updatedDetails = userDetailsService.loadUserByUsername(memberDetails.getUsername());
		log.info(updatedDetails);
		Authentication authen = new UsernamePasswordAuthenticationToken(
				updatedDetails, updatedDetails.getPassword(), updatedDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authen);
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end reloadAuth
	
}

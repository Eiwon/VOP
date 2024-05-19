package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.MemberVO;
import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/member")
@Log4j
public class MemberRESTController {
	
	@Autowired
	MemberService memberService;
	
	@PostMapping("/register") // ȸ������ ��û
	public ResponseEntity<Integer> registerPOST(@RequestBody MemberVO memberVO) {
		log.info("ȸ�� ���� ��û : " + memberVO.toString());
		int res = memberService.registerMember(memberVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerPOST
	
	@PostMapping("/idDupChk") // id �ߺ� üũ
	public ResponseEntity<Integer> checkIdDup(@RequestParam String memberId){
		log.info("member id �ߺ� üũ" + memberId);
		String checkedId = memberService.checkIdDup(memberId);
		
		int result = (checkedId == null) ? 1 : 0;
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	} // end checkIdDup
	
	@PostMapping("/phoneDupChk") // �޴��� ��ȣ�� ���Կ��� üũ
	public ResponseEntity<Integer> checkPhoneDup(@RequestParam String memberPhone){
		log.info("member phone �ߺ� üũ : " + memberPhone);
		String checkedId = memberService.getIdByPhone(memberPhone);
		
		int result = (checkedId == null) ? 1 : 0;
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	} // end checkPhoneDup
	
	@PostMapping("/findByPhone")
	public ResponseEntity<String> findByNameAndPhone( // �̸�, ��ȭ��ȣ�� id ã��
			@RequestParam String memberName, @RequestParam String memberPhone){
		log.info("�̸�, ��ȭ��ȣ�� id ã��");
		String checkedId = memberService.findByNameAndPhone(memberName, memberPhone);
		
		return new ResponseEntity<String>(checkedId, HttpStatus.OK);
	} // end findByNameAndPhone
	
	@PostMapping("/findPassword")
	public ResponseEntity<Integer> resetPassword(
			@RequestParam String memberId, @RequestParam String memberPw){
		log.info("��й�ȣ ����");
		int res = memberService.updatePw(memberId, memberPw);
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end resetPassword
	
	@PostMapping("/checkMember")
	public ResponseEntity<Integer> checkMember(@RequestParam String memberId, @RequestParam String memberPw){
		log.info("��й�ȣ Ȯ��");
		String checkedId = memberService.checkLogin(memberId, memberPw);
		
		int res = (checkedId == null) ? 0 : 1;
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end checkMember
	
	
	@PutMapping("/auth")
	public ResponseEntity<Integer> updateAuth(
			@RequestParam String memberId, @RequestParam String auth){
		log.info("���� ����");
		int res = memberService.updateAuth(memberId, auth);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateAuth
	
	
}

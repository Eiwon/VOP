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
	
	@PostMapping("/register") // 회원가입 요청
	public ResponseEntity<Integer> registerPOST(@RequestBody MemberVO memberVO) {
		log.info("회원 가입 요청 : " + memberVO.toString());
		int res = memberService.registerMember(memberVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerPOST
	
	@PostMapping("/idDupChk") // id 중복 체크
	public ResponseEntity<Integer> checkIdDup(@RequestParam String memberId){
		log.info("member id 중복 체크" + memberId);
		String checkedId = memberService.checkIdDup(memberId);
		
		int result = (checkedId == null) ? 1 : 0;
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	} // end checkIdDup
	
	@PostMapping("/phoneDupChk") // 휴대폰 번호로 가입여부 체크
	public ResponseEntity<Integer> checkPhoneDup(@RequestParam String memberPhone){
		log.info("member phone 중복 체크 : " + memberPhone);
		String checkedId = memberService.getIdByPhone(memberPhone);
		
		int result = (checkedId == null) ? 1 : 0;
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	} // end checkPhoneDup
	
	@PostMapping("/findByPhone")
	public ResponseEntity<String> findByNameAndPhone( // 이름, 전화번호로 id 찾기
			@RequestParam String memberName, @RequestParam String memberPhone){
		log.info("이름, 전화번호로 id 찾기");
		String checkedId = memberService.findByNameAndPhone(memberName, memberPhone);
		
		return new ResponseEntity<String>(checkedId, HttpStatus.OK);
	} // end findByNameAndPhone
	
	@PostMapping("/findPassword")
	public ResponseEntity<Integer> resetPassword(
			@RequestParam String memberId, @RequestParam String memberPw){
		log.info("비밀번호 변경");
		int res = memberService.updatePw(memberId, memberPw);
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end resetPassword
	
	@PostMapping("/checkMember")
	public ResponseEntity<Integer> checkMember(@RequestParam String memberId, @RequestParam String memberPw){
		log.info("비밀번호 확인");
		String checkedId = memberService.checkLogin(memberId, memberPw);
		
		int res = (checkedId == null) ? 0 : 1;
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end checkMember
	
	
	@PutMapping("/auth")
	public ResponseEntity<Integer> updateAuth(
			@RequestParam String memberId, @RequestParam String auth){
		log.info("권한 변경");
		int res = memberService.updateAuth(memberId, auth);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateAuth
	
	
}

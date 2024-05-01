package com.web.vop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.MemberVO;
import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;

// RestController에는 Controller의 기능도 모두 포함되어 있어서
// @Controller 대신 @RestController 사용
 
@RestController
@RequestMapping("/member")
@Log4j
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@GetMapping("/login")
	public void loginGET() {
		log.info("login 페이지 이동 요청");
	} // end loginGET
	
	@PostMapping("/login")
	public String loginPOST(String memberId, String memberPw, HttpServletRequest request) {
		String path = "";
		log.info("login 요청");
		
		String checkedId = memberService.checkLogin(memberId, memberPw);
		// id와 pw가 일치하는 유저의 id 검색
		
		if(checkedId != null) {
			log.info(checkedId + " 로그인 성공");
			HttpSession session = request.getSession();
			session.setAttribute("memberId", checkedId);
			session.setMaxInactiveInterval(1000);
			path = "redirect:/";
		}else {
			log.info("로그인 실패");
			path = "redirect:/member/login";
		}
		
		return null;
	} // end loginPOST
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("회원 가입 페이지 요청");
	} // end registerGET
	
	@PostMapping("/register") // 회원가입 비동기 요청
	public ResponseEntity<Integer> registerPOST(@RequestBody MemberVO memberVO) {
		log.info("회원 가입 요청");
		int res = memberService.registerMember(memberVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerPOST
	
	
	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/";
	} // end logout
	
	
	@PostMapping("/idDupChk") // id 중복 체크 (비동기)
	public ResponseEntity<Integer> checkIdDup(@RequestBody String memberId){
		log.info("member id 중복 체크");
		String checkedId = memberService.checkIdDup(memberId);
		
		int result = (checkedId == null) ? 1 : 0;
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	} // end checkIdDup
	
	
	
	
}

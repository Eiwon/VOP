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

// RestController���� Controller�� ��ɵ� ��� ���ԵǾ� �־
// @Controller ��� @RestController ���
 
@RestController
@RequestMapping("/member")
@Log4j
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@GetMapping("/login")
	public void loginGET() {
		log.info("login ������ �̵� ��û");
	} // end loginGET
	
	@PostMapping("/login")
	public String loginPOST(String memberId, String memberPw, HttpServletRequest request) {
		String path = "";
		log.info("login ��û");
		
		String checkedId = memberService.checkLogin(memberId, memberPw);
		// id�� pw�� ��ġ�ϴ� ������ id �˻�
		
		if(checkedId != null) {
			log.info(checkedId + " �α��� ����");
			HttpSession session = request.getSession();
			session.setAttribute("memberId", checkedId);
			session.setMaxInactiveInterval(1000);
			path = "redirect:/";
		}else {
			log.info("�α��� ����");
			path = "redirect:/member/login";
		}
		
		return null;
	} // end loginPOST
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("ȸ�� ���� ������ ��û");
	} // end registerGET
	
	@PostMapping("/register") // ȸ������ �񵿱� ��û
	public ResponseEntity<Integer> registerPOST(@RequestBody MemberVO memberVO) {
		log.info("ȸ�� ���� ��û");
		int res = memberService.registerMember(memberVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerPOST
	
	
	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/";
	} // end logout
	
	
	@PostMapping("/idDupChk") // id �ߺ� üũ (�񵿱�)
	public ResponseEntity<Integer> checkIdDup(@RequestBody String memberId){
		log.info("member id �ߺ� üũ");
		String checkedId = memberService.checkIdDup(memberId);
		
		int result = (checkedId == null) ? 1 : 0;
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	} // end checkIdDup
	
	
	
	
}

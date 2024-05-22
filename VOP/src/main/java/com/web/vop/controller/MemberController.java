package com.web.vop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MemberVO;
import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/member")
@Log4j
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@GetMapping("/register")
	public void registerGET() {
		System.out.println("회원가입 페이지 이동");
		log.info("회원 가입 페이지 요청");
	} // end registerGET
	
	@GetMapping("/login")
	public void loginGET() {
		System.out.println("login 페이지 이동");
		log.info("login 페이지 이동 요청");
	} // end loginGET
	
	@GetMapping("/findAccount")
	public void findAccount() {
		log.info("아이디, 비밀번호 찾기 페이지 요청");
	} // end findAccount
	
	@GetMapping("/findPassword")
	public void findPassword() {
		log.info("비밀번호 찾기 페이지 요청");
	} // end findPassword
	
	@GetMapping("/modify")
	public void modifyGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("내 정보 수정 페이지 요청");
		String memberId = memberDetails.getUsername();
		model.addAttribute("memberVO", memberService.getMemberInfo(memberId));
	} // end modifyGET
	
}

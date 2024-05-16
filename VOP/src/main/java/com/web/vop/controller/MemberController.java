package com.web.vop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.web.vop.domain.MemberVO;
import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/member")
@Log4j
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@GetMapping("/login")
	public void loginGET() {
		System.out.println("login 페이지 이동");
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
		
		return path;
	} // end loginPOST
	
	@GetMapping("/register")
	public void registerGET() {
		System.out.println("회원가입 페이지 이동");
		log.info("회원 가입 페이지 요청");
	} // end registerGET
	
	@GetMapping("/findAccount")
	public void findAccount() {
		log.info("아이디, 비밀번호 찾기 페이지 요청");
	} // end findAccount
	
	@GetMapping("/findPassword")
	public void findPassword() {
		log.info("비밀번호 찾기 페이지 요청");
	} // end findPassword
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/";
	} // end logout
	
	@GetMapping("/modify")
	public void modifyGET(Model model, HttpServletRequest request) {
		log.info("내 정보 수정 페이지 요청");
		String memberId = (String)request.getSession().getAttribute("memberId");
		model.addAttribute("memberVO", memberService.getMemberInfo(memberId));
	} // end modifyGET
	
}

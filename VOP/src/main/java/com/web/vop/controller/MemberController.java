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
		System.out.println("login ������ �̵�");
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
		
		return path;
	} // end loginPOST
	
	@GetMapping("/register")
	public void registerGET() {
		System.out.println("ȸ������ ������ �̵�");
		log.info("ȸ�� ���� ������ ��û");
	} // end registerGET
	
	@GetMapping("/findAccount")
	public void findAccount() {
		log.info("���̵�, ��й�ȣ ã�� ������ ��û");
	} // end findAccount
	
	@GetMapping("/findPassword")
	public void findPassword() {
		log.info("��й�ȣ ã�� ������ ��û");
	} // end findPassword
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/";
	} // end logout
	
	@GetMapping("/modify")
	public void modifyGET(Model model, HttpServletRequest request) {
		log.info("�� ���� ���� ������ ��û");
		String memberId = (String)request.getSession().getAttribute("memberId");
		model.addAttribute("memberVO", memberService.getMemberInfo(memberId));
	} // end modifyGET
	
}

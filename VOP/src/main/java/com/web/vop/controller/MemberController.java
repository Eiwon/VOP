package com.web.vop.controller;


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

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MemberVO;
import com.web.vop.service.MemberService;
import com.web.vop.service.UserDetailsServiceImple;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/member")
@Log4j
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	UserDetailsServiceImple UserDetailsService;
	
	@GetMapping("/register")
	public void registerGET() {
		System.out.println("ȸ������ ������ �̵�");
		log.info("ȸ�� ���� ������ ��û");
	} // end registerGET
	
	@GetMapping("/login")
	public void loginGET() {
		System.out.println("login ������ �̵�");
		log.info("login ������ �̵� ��û");
	} // end loginGET
	
	@GetMapping("/findAccount")
	public void findAccount() {
		log.info("���̵�, ��й�ȣ ã�� ������ ��û");
	} // end findAccount
	
	@GetMapping("/findPassword")
	public void findPassword() {
		log.info("��й�ȣ ã�� ������ ��û");
	} // end findPassword
	
	@PostMapping("/register") // ȸ������ ��û
	public String registerPOST(MemberVO memberVO) {
		log.info("ȸ�� ���� ��û : " + memberVO);
		int res = memberService.registerMember(memberVO);
		log.info("ȸ�� ���� ��� : " + res);
		
		String path = (res == 1) ? "redirect:login" : "redirect:register";
		
		return path;
	} // end registerPOST
	
	
	@GetMapping("/modify")
	public void modifyGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("�� ���� ���� ������ ��û");
		String memberId = memberDetails.getUsername();
		model.addAttribute("memberVO", memberService.getMemberInfo(memberId));
	} // end modifyGET

	@PostMapping("/modify")
	public String modifyPOST(MemberVO memberVO, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("ȸ�� ���� ���� : " + memberVO);
		String returnPath;
		
		String newPw = memberVO.getMemberPw();
		if(newPw.length() == 0) {
			newPw = null;
		}
		
		memberVO.setMemberId(memberDetails.getUsername());
		int res = memberService.updateMember(memberVO);
		returnPath = (res == 1) ? "redirect:../board/mypage" : "redirect:modify";
		
		return returnPath;
	} // end modifyPOST
	
}

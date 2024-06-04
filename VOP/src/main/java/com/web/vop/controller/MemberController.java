package com.web.vop.controller;


import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MemberVO;
import com.web.vop.service.MailAuthenticationService;
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
	
	@Autowired
	MailAuthenticationService mailAuthService;
	
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
	
	@PostMapping("/findAccount")
	public String mailAuthenticationPOST(Model model, String memberEmail, String authCode){
		log.info("���̵� ã�� ���� �ڵ� :" + authCode);
		
		String returnPath = null;
		List<String> memberIdList = null;
		boolean res = mailAuthService.verifyAuthCode(memberEmail, authCode);
		
		if(res) {
			memberIdList = memberService.getIdByEmail(memberEmail);
			log.info("�˻��� id : " + memberIdList);
			model.addAttribute("memberIdList", memberIdList);
			returnPath = "member/findAccountResult";
		}else {
			log.info("�����ڵ� ����ġ");
			returnPath = "redirect:findAccount";
		}
		
		return returnPath;
	} // end mailAuthenticationPOST
	
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

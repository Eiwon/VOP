package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		log.info("/member/loging get");
	} // end loginGET
	
	@PostMapping("/login")
	public String loginPOST(String memberId, String memberPw) {
		return null;
	} // end loginPOST
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("/member/register get");
	} // end registerGET
	
	
}

package com.web.vop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;


@Controller
@Log4j
public class BoardController {// 메인 페이지 구현 컨트롤러

	@GetMapping("/main") 
	public void mainGET() {
		System.out.println("main.jsp 이동");
		log.info("mainGET()");
	}//end mainGET()
	
	
	@PostMapping("/main")
	public String mainPOST() {
		log.info("mainPOST()");
		
		return "redirect:/board/main";
	}
	
	
	
	// 마이페이지 호출하기 -> 세션이 있을 경우 이동, 없으면 로그인 페이지로 이동
	@GetMapping("/mypage") 
	public void mypageGET() {
		System.out.println("mypage.jsp 이동");
		log.info("mypageGET()");
	}//end mypageGET()
	
	
	@Autowired
	private MemberService memberService;
	
	@PostMapping("/mypage")
	public String mypagePOST() {
		log.info("mypagePOST()");
		//if () {
			
		//}else {			
			return "redirect:/board/register";
		//}
	}
	
	
	// 장바구니 호출하기
	@GetMapping("/basket") 
	public void basketGET() {
		System.out.println("basket.jsp 이동");
		log.info("basketGET()");
	}//end basketGET()
	
	
	@PostMapping("/basket")
	public String basketPOST() {
		log.info("basketPOST()");
		
		return "redirect:/board/basket";
	}
	
	
	// 고객센터 호출하기
	@GetMapping("inquiry") 
	public void inquiryGET() {
		System.out.println("inquiry.jsp 이동");
		log.info("inquiryGET()");
	}//end inquiryGET()
	
	
	@PostMapping("/inquiry")
	public String inquiryPOST() {
		log.info("inquiryPOST()");
		
		return "redirect:/board/inquiry";
	}
		
		
}//end MainController

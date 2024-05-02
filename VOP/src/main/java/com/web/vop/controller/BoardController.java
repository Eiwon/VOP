package com.web.vop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;


@Controller
@Log4j
public class BoardController {// ���� ������ ���� ��Ʈ�ѷ�

	@GetMapping("/main") 
	public void mainGET() {
		System.out.println("main.jsp �̵�");
		log.info("mainGET()");
	}//end mainGET()
	
	
	@PostMapping("/main")
	public String mainPOST() {
		log.info("mainPOST()");
		
		return "redirect:/board/main";
	}
	
	
	
	// ���������� ȣ���ϱ� -> ������ ���� ��� �̵�, ������ �α��� �������� �̵�
	@GetMapping("/mypage") 
	public void mypageGET() {
		System.out.println("mypage.jsp �̵�");
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
	
	
	// ��ٱ��� ȣ���ϱ�
	@GetMapping("/basket") 
	public void basketGET() {
		System.out.println("basket.jsp �̵�");
		log.info("basketGET()");
	}//end basketGET()
	
	
	@PostMapping("/basket")
	public String basketPOST() {
		log.info("basketPOST()");
		
		return "redirect:/board/basket";
	}
	
	
	// ������ ȣ���ϱ�
	@GetMapping("inquiry") 
	public void inquiryGET() {
		System.out.println("inquiry.jsp �̵�");
		log.info("inquiryGET()");
	}//end inquiryGET()
	
	
	@PostMapping("/inquiry")
	public String inquiryPOST() {
		log.info("inquiryPOST()");
		
		return "redirect:/board/inquiry";
	}
		
		
}//end MainController

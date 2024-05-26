package com.web.vop.controller;


import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.OrderVO;
import com.web.vop.service.MemberService;
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;

@RequestMapping("/board")
@Controller
@Log4j
public class BoardController {// ���� ������ ���� ��Ʈ�ѷ�

	//@Autowired
	//private MemberService memberService;
	
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
	public void mypageGET(Model model) {
		System.out.println("mypage.jsp �̵�");
		log.info("mypageGET()");
	}//end mypageGET()
	
	
	
	
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
	
	@GetMapping("/myInfo")
	public String myInfoGET() {
		log.info("member/modify�� redirect");
		return "redirect:../member/modify";
	} // end myInfoGet
	
	@GetMapping("/seller")
	public String sellerGET() {
		log.info("seller/sellerRequest�� redirect");
		return "redirect:../seller/sellerRequest";
	} // end myInfoGet
	
	@GetMapping("/admin")
	public String adminGET() {
		log.info("������ �������� �̵�");
		return "redirect:../seller/admin";
	} // end myInfoGet
	
	@GetMapping("/basket")
	public String basketGET() {
		log.info("��ٱ��� ������ �̵�");
		return "redirect:../basket/main";
	} // end basketGET
	
	@GetMapping("/delivery")
	public String deliveryGET() {
		log.info("delivery controller�� redirection");
		return "redirect:../Delivery/delivery";
	} // end deliveryGET
	
}//end MainController

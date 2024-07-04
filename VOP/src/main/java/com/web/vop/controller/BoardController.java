package com.web.vop.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.WebSocketHandler;

import com.web.vop.socket.AlarmHandler;

import lombok.extern.log4j.Log4j;

@RequestMapping("/board")
@Controller
@Log4j
public class BoardController {// 메인 페이지 구현 컨트롤러
	
	@GetMapping("/main") 
	public void mainGET() {
		System.out.println("main.jsp 이동");
		log.info("mainGET()");
	}//end mainGET()
	
	// 마이페이지 호출하기
	@GetMapping("/mypage") 
	public void mypageGET() {
		System.out.println("mypage.jsp 이동");
		log.info("mypageGET()");
	}//end mypageGET()
	
	
	// 고객센터 호출하기
	@GetMapping("inquiry") 
	public void inquiryGET() {
		System.out.println("inquiry.jsp 이동");
		log.info("inquiryGET()");
	}//end inquiryGET()
	
	@GetMapping("/myInfo")
	public String myInfoGET() {
		log.info("member/modify로 redirect");
		return "redirect:../member/modify";
	} // end myInfoGet
	
	@GetMapping("/admin")
	public String adminGET() {
		log.info("관리자 페이지로 이동");
		return "redirect:../seller/admin";
	} // end myInfoGet
	
	@GetMapping("/basket")
	public String basketGET() {
		log.info("장바구니 페이지 이동");
		return "redirect:../basket/main";
	} // end basketGET
	
	@GetMapping("/delivery")
	public String deliveryGET() {
		log.info("delivery controller로 redirection");
		return "redirect:../Delivery/deliveryAddressList";
	} // end deliveryGET
	
	@GetMapping("/consult")
	public String consultGET() {
		log.info("consultGET");
		
		return "chat/consult";
	} // end consultGET
	
	@GetMapping("/consultAccept")
	public String consultAcceptGET(Model model, String roomId){
		log.info("request room Id : " + roomId);
		
		model.addAttribute("roomId", roomId);
		
		return "chat/consult";
	} // end consultAcceptGET
	
}//end MainController

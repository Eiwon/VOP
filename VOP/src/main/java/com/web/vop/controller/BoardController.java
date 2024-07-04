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
public class BoardController {// ���� ������ ���� ��Ʈ�ѷ�
	
	@GetMapping("/main") 
	public void mainGET() {
		System.out.println("main.jsp �̵�");
		log.info("mainGET()");
	}//end mainGET()
	
	// ���������� ȣ���ϱ�
	@GetMapping("/mypage") 
	public void mypageGET() {
		System.out.println("mypage.jsp �̵�");
		log.info("mypageGET()");
	}//end mypageGET()
	
	
	// ������ ȣ���ϱ�
	@GetMapping("inquiry") 
	public void inquiryGET() {
		System.out.println("inquiry.jsp �̵�");
		log.info("inquiryGET()");
	}//end inquiryGET()
	
	@GetMapping("/myInfo")
	public String myInfoGET() {
		log.info("member/modify�� redirect");
		return "redirect:../member/modify";
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

package com.web.vop.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.service.MembershipService;
import com.web.vop.util.PaymentAPIUtil;

import lombok.extern.log4j.Log4j;

@RequestMapping("/membership")
@Controller
@Log4j
public class MembershipController {

	@Autowired
	MembershipService membershipService;
	
	@Autowired
	private PaymentAPIUtil paymentAPIUtil;
	
	@GetMapping("/register")
	public String registerGET(@AuthenticationPrincipal MemberDetails memberDetails,
			Model model){
		log.info("멤버십 등록 페이지 이동");
		String memberId = memberDetails.getUsername();
		log.info(memberId);
		String memberAuth = memberDetails.getAuth();
		log.info(memberAuth);
	
		
		if(memberAuth.equals("멤버십")) { // 유저 권한이 멤버십인 경우
			log.info("success 페이지로 이동");
			 return "membership/success"; // // 성공 페이지로 리디렉션
		}else if(memberAuth.equals("일반")){ // 유저 권한이 일반인 경우
			log.info("일반 페이지로 이동");
			
			PaymentWrapper payment = membershipService.makeMembershipForm(memberId);
			log.info("paymentWrapper 객체 : " + payment);
			
			try { // 자바스크립트에서 쓰기 위해 json 형식 문자열로 변환
				model.addAttribute("paymentWrapper", new ObjectMapper().writeValueAsString(payment));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			return "membership/register"; // 등록 페이지로 리디렉션
		}else {
			log.info("접근 불가");
			return " ";
		}		
		
	}//end registerGET()
	
	
	@GetMapping("/success")
	public void successGET() {
		log.info("멤버십 등록 성공");
	}
	
	
	
	
}

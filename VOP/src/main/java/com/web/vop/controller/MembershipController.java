package com.web.vop.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.amazonaws.services.transfer.model.UserDetails;
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
	
	@PreAuthorize("!hasRole('판매자')") 
	@GetMapping("/register")
	public String registerGET(Model model,@AuthenticationPrincipal UserDetails memberDetails){ 
		log.info("멤버십 등록 페이지 이동");
		String memberId = memberDetails.getUserName(); // memberDetails 안쓰면 이름을 어디서 가지고 오지? 
		log.info("memberId : " + memberId);
		// getAuth() 삭제!

		PaymentWrapper payment = membershipService.makeMembershipForm(memberId);
		log.info("paymentWrapper 객체 : " + payment);
		
		try { // 자바스크립트에서 쓰기 위해 json 형식 문자열로 변환
			model.addAttribute("paymentWrapper", new ObjectMapper().writeValueAsString(payment));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return "membership/register"; // 등록 페이지로 리디렉션		
	}//end registerGET()
	
	
	@GetMapping("/success")
	public void successGET() {
		log.info("멤버십 등록 성공");
	}
	
	
	
	
}

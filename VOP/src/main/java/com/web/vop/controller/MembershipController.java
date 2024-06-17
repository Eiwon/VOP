package com.web.vop.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
	
<<<<<<< HEAD
	@Autowired
	private PaymentAPIUtil paymentAPIUtil;
	
	@GetMapping("/register")
	public String registerGET(@AuthenticationPrincipal MemberDetails memberDetails,
			Model model){
		log.info("멤버십 등록 페이지 이동");
		String memberId = memberDetails.getUsername();
		log.info(memberId);
		String memberAuth = memberDetails.getAuth();
=======
	// 권한 설정은 시큐리티로 해주세요
	// 권한 있는 사용자만 접근 가능 : securityConfig 수정 or 여기에 @PreAuthorize("!hasRole('판매자')") 추가
	// 권한에 따라 다른 페이지로 이동시키는 건, 일단 하나의 페이지로 이동시키고, sec: 태그로 권한 검사해서 redirect 시키는게 좋을것 같아요 
	@GetMapping("/register")
	public String registerGET(@AuthenticationPrincipal MemberDetails memberDetails){ // <- UserDetails 타입으로 선언해야 합니다
		
		log.info("멤버십 등록 페이지 이동");
		
		String memberAuth = memberDetails.getAuth(); // <- UserDetails 인터페이스에서 제공하는 메소드가 아니라 편하게 쓰려고 만든 건데 안쓰는게 좋대요
													// 수정후 MemberDetails.java에서 삭제해주세요
>>>>>>> ef0c7002d5dac7e68e9f6991cfb1945946ccedbb
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

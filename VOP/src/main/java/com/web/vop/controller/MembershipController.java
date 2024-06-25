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
	
	
	@PreAuthorize("isAuthenticated()") // ����ڰ� �α��� ���¶�� true
	@GetMapping("/register")
	public String registerGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails){ 
		log.info("����� ��� ������ �̵�");
		  if (memberDetails == null) {
		        log.error("UserDetails is null");
		        throw new IllegalArgumentException("UserDetails cannot be null");
		    }
		String memberId = memberDetails.getUsername();
		log.info("memberId : " + memberId);
		
		// getAuth() ����!

		PaymentWrapper payment = membershipService.makeMembershipForm(memberId);
		log.info("paymentWrapper ��ü : " + payment);
		
		try { // �ڹٽ�ũ��Ʈ���� ���� ���� json ���� ���ڿ��� ��ȯ
			model.addAttribute("paymentWrapper", new ObjectMapper().writeValueAsString(payment));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return "membership/register"; // ��� �������� ���𷺼�		
	}//end registerGET()
	
	
	@GetMapping("/success")
	public void successGET() {
		log.info("����� ��� ����");
	}
	
	
	
	
}
